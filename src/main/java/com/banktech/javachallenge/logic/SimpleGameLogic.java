package com.banktech.javachallenge.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.banktech.javachallenge.Main;
import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.ProjectedPosition;
import com.banktech.javachallenge.service.domain.Target;
import com.banktech.javachallenge.service.domain.game.MapConfiguration;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.Entity;
import com.banktech.javachallenge.service.domain.submarine.EntityType;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.ShootRequest;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;
import com.banktech.javachallenge.service.world.World;
import com.banktech.javachallenge.view.domain.ApiCall;
import com.banktech.javachallenge.view.domain.ViewModel;

public class SimpleGameLogic implements GameLogic {
    private ViewModel viewModel;
    private MapConfiguration mapConfiguration;
    private Map<Long, Target> submarineDesiredDestination = new HashMap<>();

    @Override
    public synchronized ViewModel step(ViewModel currentViewModel) throws IOException {
        fillAttributes(currentViewModel);
        List<OwnSubmarine> ownSubmarines = currentViewModel.getOwnSubmarines();
        for (OwnSubmarine ownSubmarine : ownSubmarines) {
            handleSonar(currentViewModel.getWorldMap(), ownSubmarine);
        }
        for (OwnSubmarine ownSubmarine : ownSubmarines) {
            handleSubmarineMove(currentViewModel.getWorldMap(), ownSubmarine);
        }
        return viewModel;
    }

    private void handleSonar(World worldMap, OwnSubmarine ownSubmarine) throws IOException {
        if (!weHaveSonar() && ownSubmarine.getSonarCooldown() == 0) {
            handleSonarResponse(extendedSonar(worldMap, ownSubmarine));
        }
        handleSonarResponse(sonar(worldMap, ownSubmarine));
    }

    private void fillAttributes(ViewModel currentViewModel) {
        viewModel = currentViewModel;
        mapConfiguration = currentViewModel.getGame().getMapConfiguration();
    }

    public void handleSubmarineMove(World world, OwnSubmarine submarine) throws IOException {
        if (submarine.getTorpedoCooldown() == 0) {
            List<ProjectedPosition> projectedLocations = submarine.torpedoPathInRounds(mapConfiguration.getTorpedoRange(), mapConfiguration.getTorpedoSpeed());
            if (closeEnemy(submarine, projectedLocations)) {
                shoot(world, submarine);
            }
        }
        double speedChange = maxAcceleration(submarine);
        double angle = avoidCollision(world, submarine);
        move(world, submarine, speedChange, angle);
    }

    private boolean weHaveSonar() {
        return viewModel.getOwnSubmarines().stream().filter(s -> s.getSonarExtended() > 0).findAny().isPresent();
    }

    private boolean closeEnemy(OwnSubmarine ownSubmarine, List<ProjectedPosition> projectedLocations) {
        Optional<Entity> enemyPresent = viewModel.getDetectedSubmarines().stream()
                .filter(e -> close(projectedLocations, e))
                .findAny();
        boolean friendPresent = viewModel.getOwnSubmarines().stream()
                .filter(e -> !e.getId().equals(ownSubmarine.getId()))
                .filter(e -> close(projectedLocations, e))
                .findAny().isPresent();
        return enemyPresent.isPresent()
                && enemyPresent.get().getPosition().distance(ownSubmarine.getPosition()) > mapConfiguration.getTorpedoExplosionRadius()
                && !friendPresent;
    }

    private boolean close(List<ProjectedPosition> projectedLocations, Entity e) {
        List<ProjectedPosition> enemyPosition = e.pathInRounds(mapConfiguration.getTorpedoRange());
        double min = Double.MAX_VALUE;
        for (int i = 0; i < enemyPosition.size(); i++) {
            double distance = projectedLocations.get(i).getPosition().distance(enemyPosition.get(i).getPosition());
            if (min > distance) {
                min = distance;
            }
            if (distance < mapConfiguration.getSubmarineSize()) {
                return true;
            }
        }
        return false;
    }

    private SonarResponse sonar(World world, OwnSubmarine submarine) throws IOException {
        SonarResponse response = world.sonar(submarine);
        viewModel.getCalls().add(new ApiCall(Api.SONAR, submarine.getId(), response));
        return response;
    }

    private SonarResponse extendedSonar(World world, OwnSubmarine submarine) throws IOException {
        SonarResponse response = world.extendedSonar(submarine);
        viewModel.getCalls().add(new ApiCall(Api.EXTENDED_SONAR, submarine.getId(), response));
        return response;
    }

    private void handleSonarResponse(SonarResponse sonarResponse) {
        if (sonarResponse != null) {
            List<Entity> detectedSubmarines = sonarResponse.getEntities().stream()
                    .filter(entity -> !entity.getOwner().getName().equals(Main.ourTeamName()))
                    .filter(entity -> entity.getType().equals(EntityType.Submarine)).collect(Collectors.toList());
            viewModel.setDetectedSubmarines(detectedSubmarines);

            List<Entity> detectedTorpedos = sonarResponse.getEntities().stream()
                    .filter(entity -> entity.getType().equals(EntityType.Torpedo)).collect(Collectors.toList());

            viewModel.setDetectedTorpedos(detectedTorpedos);

            sonarResponse.getEntities().stream()
                    .filter(entity -> entity.getOwner().getName().equals(Main.ourTeamName()))
                    .filter(entity -> entity.getType().equals(EntityType.Submarine)).collect(Collectors.toList())
                    .forEach(entity -> viewModel.getWorldMap().replaceCell(entity.getPosition(), entity));
        } else {
            System.out.println("VALAMI HIBA VAN!!! handleSonarResponse-ban");
        }
    }

    private void shoot(World world, OwnSubmarine submarine) throws IOException {
        SimpleResponse response = world.shoot(submarine, new ShootRequest(submarine.getAngle()));
        viewModel.getCalls().add(new ApiCall(Api.SHOOT, submarine.getId(), response));
    }

    private void move(World world, OwnSubmarine submarine, double speedChange, double angle) throws IOException {
        SimpleResponse response = world.move(submarine, new MoveRequest(speedChange, angle));
        viewModel.getCalls().add(new ApiCall(Api.MOVE, submarine.getId(), response));
    }

    private double avoidCollision(World world, OwnSubmarine submarine) {
        Position newDestination = getDesiredDestination(submarine);
        double angle = submarine.getPosition().angleTo(newDestination);
        double adjustedAngle = adjustAngleObstacle(submarine, angle);
        double change = adjustedAngle - submarine.getAngle();
        if (Math.abs(change) > 1) {
            return adjustAngle(change);
        }
        return 0;
    }

    private double adjustAngleObstacle(OwnSubmarine submarine, double angle) {
        double plusAngle = adjustAngleToSafePath(submarine, angle, +1);
        double minusAngle = adjustAngleToSafePath(submarine, angle, -1);
        if (plusAngle - angle < angle - minusAngle) {
            return plusAngle;
        } else {
            return minusAngle;
        }
    }

    private double adjustAngleToSafePath(OwnSubmarine submarine, double angle, int adjust) {
        int tries = 0;
        double adjustedAngle = angle;
        List<ProjectedPosition> projectedLocations = submarine.pathInRounds(10, adjustedAngle);
        while (offWater(projectedLocations) && tries++ < 180) {
            adjustedAngle += adjust;
            projectedLocations = submarine.pathInRounds(10, adjustedAngle);
        }
        return adjustedAngle;
    }

    private double adjustAngle(double change) {
        double result;
        if (change > 180) {
            change -= 360;
        } else if (change < -180) {
            change += 360;
        }
        if (change > 0) {
            result = Math.min(change, mapConfiguration.getMaxSteeringPerRound());
        } else {
            result = Math.max(change, -mapConfiguration.getMaxSteeringPerRound());
        }
        return result;
    }

    private Position getDesiredDestination(OwnSubmarine submarine) {
        Target desiredDestination = submarineDesiredDestination.get(submarine.getId());
        if (desiredDestination != null) {
            List<Long> ids = viewModel.getDetectedSubmarines().stream().map(e -> e.getId()).collect(Collectors.toList());
            if (ids.contains(desiredDestination.getTargetId())) {
                desiredDestination = targetEnemy(submarine, find(viewModel.getDetectedSubmarines(), desiredDestination.getTargetId()));
                return desiredDestination.getPosition();
            }
        }
        Optional<Entity> closest = viewModel.getDetectedSubmarines().stream()
                .min((a, b) -> Double.compare(a.getPosition().distance(submarine.getPosition()), b.getPosition().distance(submarine.getPosition())));
        if (closest.isPresent()) {
            desiredDestination = targetEnemy(submarine, closest.get());
        }
        if (desiredDestination == null) {
            Position desiredPosition = mapConfiguration.getIslandPositions().get(0).copy();
            desiredPosition.setX(desiredPosition.getX() + 200);
            desiredPosition.setY(desiredPosition.getY() + 200);
            return desiredPosition;
        }
        return desiredDestination.getPosition();
    }

    private Entity find(List<Entity> detectedSubmarines, Long targetId) {
        return detectedSubmarines.stream().filter(e -> e.getId().equals(targetId)).findAny().get();
    }

    private Target targetEnemy(OwnSubmarine submarine, Entity enemy) {
        double distance = enemy.getPosition().distance(submarine.getPosition());
        List<ProjectedPosition> path = enemy.pathInRounds(distance / mapConfiguration.getTorpedoSpeed());
        Position position = path.get(path.size() - 1).getPosition();
        Target desiredDestination = new Target(enemy.getId(), position);
        submarineDesiredDestination.put(submarine.getId(), desiredDestination);
        return desiredDestination;
    }

    private boolean offWater(List<ProjectedPosition> projectedLocations) {
        for (ProjectedPosition position : projectedLocations) {
            if (offWater(position.getPosition())) {
                return true;
            }
        }
        return false;
    }

    private boolean offWater(Position projectedLocation) {
        return projectedLocation.outSide(mapConfiguration) || projectedLocation.island(mapConfiguration);
    }

    private double maxAcceleration(OwnSubmarine submarine) {
        double speedChange = 0;
        if (submarine.getVelocity() < mapConfiguration.getMaxSpeed()) {
            speedChange = Math.min(mapConfiguration.getMaxSpeed() - submarine.getVelocity(), mapConfiguration.getMaxAccelerationPerRound());
        }
        return speedChange;
    }

}
