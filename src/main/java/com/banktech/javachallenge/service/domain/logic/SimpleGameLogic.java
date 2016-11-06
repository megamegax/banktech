package com.banktech.javachallenge.service.domain.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.game.MapConfiguration;
import com.banktech.javachallenge.service.domain.submarine.MoveRequest;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.view.domain.ViewModel;
import com.banktech.javachallenge.world.World;

public class SimpleGameLogic implements GameLogic {

    private MapConfiguration mapConfiguration;
    private Map<Long, Double> submarineDesiredAngle = new HashMap<>();

    @Override
    public synchronized void step(ViewModel currentViewModel) throws IOException {
        fillAttributes(currentViewModel);
        List<OwnSubmarine> ownSubmarines = currentViewModel.getOwnSubmarines();
        for (OwnSubmarine ownSubmarine : ownSubmarines) {
            handleSubmarine(currentViewModel.getWorldMap(), ownSubmarine);
        }
    }

    private void fillAttributes(ViewModel currentViewModel) {
        mapConfiguration = currentViewModel.getGame().getMapConfiguration();
    }

    public void handleSubmarine(World world, OwnSubmarine submarine) throws IOException {
        System.out.println(submarine);
        double speedChange = maxAcceleration(submarine);
        double angle = avoidCollision(world, submarine);
        world.move(submarine, new MoveRequest((double) speedChange, angle));
        world.sonar(submarine);
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
            if (change > 180) {
                change -= 360;
            } else if (change < -180) {
                change += 360;
            }
            if (change > 0) {
                double result = Math.min(change, mapConfiguration.getMaxSteeringPerRound());
                System.out.println("" + submarine.getId() + ": " + result + "->" + submarine.getAngle() + "->" + newAngle);
                return result;
            } else {
                double result = Math.max(change, -mapConfiguration.getMaxSteeringPerRound());
                System.out.println("" + submarine.getId() + ": " + result + "->" + submarine.getAngle() + "->" + newAngle);
                return result;
            }
        }
        return 0;
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
