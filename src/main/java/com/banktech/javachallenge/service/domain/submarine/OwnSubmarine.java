package com.banktech.javachallenge.service.domain.submarine;


public class OwnSubmarine extends Submarine {
    private Integer hp;
    private Integer sonarCooldown;
    private Integer torpedoCooldown;
    private Integer sonarExtended;

    public OwnSubmarine() {
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getSonarCooldown() {
        return sonarCooldown;
    }

    public void setSonarCooldown(Integer sonarCooldown) {
        this.sonarCooldown = sonarCooldown;
    }

    public Integer getTorpedoCooldown() {
        return torpedoCooldown;
    }

    public void setTorpedoCooldown(Integer torpedoCooldown) {
        this.torpedoCooldown = torpedoCooldown;
    }

    public Integer getSonarExtended() {
        return sonarExtended;
    }

    public void setSonarExtended(Integer sonarExtended) {
        this.sonarExtended = sonarExtended;
    }

    @Override
    public String toString() {
        return "OwnSubmarine{" +
                " type='" + type +
                ", id=" + id +
                ", position=" + position +
                ", owner=" + owner +
                ", velocity=" + velocity +
                ", angle=" + angle +
                ", hp=" + hp +
                ", sonarCooldown=" + sonarCooldown +
                ", torpedoCooldown=" + torpedoCooldown +
                ", sonarExtended=" + sonarExtended +
                "} ";
    }


}
