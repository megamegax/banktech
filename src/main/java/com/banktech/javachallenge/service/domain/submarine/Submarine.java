package com.banktech.javachallenge.service.domain.submarine;


import com.banktech.javachallenge.service.domain.Position;

public class Submarine {
    protected String type;
    protected Long id;
    protected Position position;
    protected Owner owner;
    protected Integer velocity;
    protected Double angle;

    public Submarine() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public Integer getVelocity() {
        return velocity;
    }

    public void setVelocity(Integer velocity) {
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
        return "Submarine{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", position=" + position +
                ", owner=" + owner +
                ", velocity=" + velocity +
                ", angle=" + angle +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Submarine)) return false;

        Submarine submarine = (Submarine) o;

        if (getType() != null ? !getType().equals(submarine.getType()) : submarine.getType() != null) return false;
        if (getId() != null ? !getId().equals(submarine.getId()) : submarine.getId() != null) return false;
        if (getPosition() != null ? !getPosition().equals(submarine.getPosition()) : submarine.getPosition() != null)
            return false;
        if (getOwner() != null ? !getOwner().equals(submarine.getOwner()) : submarine.getOwner() != null) return false;
        if (getVelocity() != null ? !getVelocity().equals(submarine.getVelocity()) : submarine.getVelocity() != null)
            return false;
        return getAngle() != null ? getAngle().equals(submarine.getAngle()) : submarine.getAngle() == null;

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
