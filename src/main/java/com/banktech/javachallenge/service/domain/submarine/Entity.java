package com.banktech.javachallenge.service.domain.submarine;

import com.banktech.javachallenge.service.domain.Position;
import com.banktech.javachallenge.service.domain.ProjectedPosition;
import com.banktech.javachallenge.view.gui.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected EntityType type;
    protected Long id;
    protected Position position;
    protected Owner owner;
    protected Double velocity;
    protected Double angle;

    public Entity() {
    }

    public List<ProjectedPosition> torpedoPathInRounds(double rounds, double speed) {
        return pathInRounds(rounds, angle, speed);
    }

    public List<ProjectedPosition> pathInRounds(double rounds) {
        return pathInRounds(rounds, angle, velocity);
    }

    public List<ProjectedPosition> pathInRounds(double rounds, double newAngle) {
        return pathInRounds(rounds, newAngle, velocity);
    }

    public List<ProjectedPosition> pathInRounds(double rounds, double newAngle, double speed) {
        List<ProjectedPosition> positions = new ArrayList<>();
        for (double d = 0; d < rounds; d += 0.1) {
            positions.add(locationInRounds(d, newAngle, speed));
        }
        return positions;
    }

    public ProjectedPosition locationInRounds(double rounds) {
        return locationInRounds(rounds, angle, velocity);
    }

    public ProjectedPosition locationInRounds(double rounds, double newAngle, double speed) {
        Position projected = new Position(position.getX() + Math.cos(MapUtil.angleToRadian(newAngle)) * speed * rounds,
                position.getY() + Math.sin(MapUtil.angleToRadian(newAngle)) * speed * rounds);
        return new ProjectedPosition(projected, rounds);
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Entity{" + "type='" + type + '\'' + ", id=" + id + ", position=" + position + ", owner=" + owner + ", velocity=" + velocity
                + ", angle=" + angle + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return getId().equals(entity.getId());

    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
        result = 31 * result + (getVelocity() != null ? getVelocity().hashCode() : 0);
        result = 31 * result + (getAngle() != null ? getAngle().hashCode() : 0);
        return result;
    }
}
