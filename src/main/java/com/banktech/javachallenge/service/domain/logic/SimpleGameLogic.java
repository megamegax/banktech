package com.banktech.javachallenge.service.domain.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.banktech.javachallenge.Main;
import com.banktech.javachallenge.service.Api;
import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.MapConfiguration;
import com.banktech.javachallenge.service.domain.game.SimpleResponse;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.domain.submarine.SonarResponse;
import com.banktech.javachallenge.view.domain.ApiCall;
import com.banktech.javachallenge.view.domain.ViewModel;
import com.banktech.javachallenge.world.World;

public class SimpleGameLogic implements GameLogic {
    private ViewModel viewModel;
    private MapConfiguration mapConfiguration;
    private Map<Long, Double> submarineDesiredAngle = new HashMap<>();

    @Override
    public synchronized ViewModel step(ViewModel currentViewModel) throws IOException {
        fillAttributes(currentViewModel);
        List<OwnSubmarine> ownSubmarines = currentViewModel.getOwnSubmarines();
        for (OwnSubmarine ownSubmarine : ownSubmarines) {
            handleSubmarine(currentViewModel.getWorldMap(), ownSubmarine);
        }
        return viewModel;
    }

    private void fillAttributes(ViewModel currentViewModel) {
        viewModel = currentViewModel;
        mapConfiguration = currentViewModel.getGame().getMapConfiguration();
    }

    public void handleSubmarine(World world, OwnSubmarine submarine) throws IOException {
        double speedChange = maxAcceleration(submarine);
        double angle = avoidCollision(world, submarine);
        
        if (submarine.getSonarCooldown() == 0) {        	
        	world.extendedSonar(submarine);
        }
        SonarResponse sonarResponse = world.sonar(submarine);
        handleSonarResponse(sonarResponse);
        move(world, submarine, speedChange, angle);
    }

    private void move(World world, OwnSubmarine submarine, double speedChange, double angle) throws IOException {
        SimpleResponse response = world.move(submarine, new MoveRequest(speedChange, angle));
        viewModel.getCalls().add(new ApiCall(Api.MOVE, submarine.getId(), response));
    }

    private void handleSonarResponse(SonarResponse sonarResponse) {
		viewModel.setDetectedSubmarines(sonarResponse.getEntities().stream()
				.filter(submarine -> !submarine.getOwner().getName().equals(Main.ourTeamName())).collect(Collectors.toList()));
		sonarResponse.getEntities().forEach(submarine -> {
			if (submarine.getOwner().getName().equals(Main.ourTeamName()))
				viewModel.getWorldMap().replaceCell(submarine.getPosition(), submarine);
		});
    }

    private double avoidCollision(World world, OwnSubmarine submarine) {
        double newAngle = getDesiredAngle(submarine);
        List<Position> projectedLocations = submarine.pathInRounds(10, newAngle);
        int tries = 0;
        while (offWater(projectedLocations) && tries < 20) {
            newAngle = Math.random() * 360;
            projectedLocations = submarine.pathInRounds(10, newAngle);
            tries++;
        }
        setDesiredAngle(submarine, newAngle);
        double change = newAngle - submarine.getAngle();
        if (Math.abs(change) > 1) {
            return adjustAngle(change);
        }
        return 0;
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

    private void setDesiredAngle(OwnSubmarine submarine, double angle) {
        submarineDesiredAngle.put(submarine.getId(), angle);
    }

    private double getDesiredAngle(OwnSubmarine submarine) {
        Double desiredAngle = submarineDesiredAngle.get(submarine.getId());
        if (desiredAngle == null) {
            desiredAngle = submarine.getAngle();
        }
        return desiredAngle;
    }

    private boolean offWater(List<Position> projectedLocations) {
        for (Position position : projectedLocations) {
            if (offWater(position)) {
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
