package com.banktech.javachallenge.service.domain.submarine;

public class ShootRequest {

    private Double angle;

    public ShootRequest() {
    }

    public ShootRequest(Double angle) {
        this.angle = angle;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "ShootRequest{" +
                "angle=" + angle +
                '}';
    }
}
