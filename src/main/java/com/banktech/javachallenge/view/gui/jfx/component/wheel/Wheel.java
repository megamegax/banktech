package com.banktech.javachallenge.view.gui.jfx.component.wheel;

import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Wheel extends AnchorPane {
    Double offset = 30.0;
    Circle indicator = new Circle(3.0);
    Circle background = new Circle(40.0, Color.LIGHTGRAY);
    private Double direction = 0.0;

    public Wheel() {
        getChildren().add(background);
        getChildren().add(indicator);
        indicator.setCenterX(background.getCenterX() + offset);
        indicator.setCenterY(background.getCenterY());
        setDirection(0.0);
        setOnMousePressed(event -> update(new Point2D(event.getX(), event.getY())));
        setOnMouseDragged(event -> update(new Point2D(event.getX(), event.getY())));

    }

    private void update(Point2D input) {
        Point2D baseLine = new Point2D(1, 0);
        int negate = input.getY() > 0 ? 1 : -1;
        double angle = input.angle(baseLine) * negate;
        setDirection(angle);
    }

    public void setDirection(Double theta) {
        double rad = Math.toRadians(theta);
        double x = offset * Math.cos(rad) + background.getCenterX();
        double y = offset * Math.sin(rad) + background.getCenterY();
        indicator.setCenterX(x);
        indicator.setCenterY(y);
        direction = theta;
    }
}
