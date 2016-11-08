package com.banktech.javachallenge.view.gui.jfx.component.submarine;

import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.world.World;
import com.banktech.javachallenge.view.gui.jfx.component.wheel.Wheel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class SubmarineControlPanelController implements Initializable {
    @FXML
    AnchorPane wheelContainer;
    private Wheel wheel;

    @FXML
    Label submarineName;

    @FXML
    Slider submarineSpeedSlider;

    @FXML
    ProgressBar hpBar;

    @FXML
    ProgressBar torpedoCooldownBar;

    @FXML
    ProgressBar sonarCooldownBar;

    double originalHp = 100;

    private World world;
    private Game game;
    private OwnSubmarine submarine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wheel = new Wheel();
        wheelContainer.getChildren().add(wheel);
    }

    public SubmarineControlPanelController() {

    }

    public void initGame(Game game) {
        this.game = game;

    }

    public void initWorld(World world) {
        this.world = world;
    }

    public void initSubmarine(OwnSubmarine submarine) {
        this.submarine = submarine;
        originalHp = submarine.getHp();
        Platform.runLater(() -> submarineName.setText(String.valueOf(submarine.getId())));
    }

    public Long getSubmarineId() {
        return submarine.getId();
    }

    public void refresh(OwnSubmarine submarine) {
        this.submarine = submarine;
        Platform.runLater(() -> {
            wheel.setDirection(submarine.getAngle());
            submarineSpeedSlider.adjustValue((submarine.getVelocity()/game.getMapConfiguration().getMaxSpeed())*100);
            calculateTorpedoCooldown();
            calculateExtendedSonarCooldown();
            calculateHp();
        });
    }

    private void calculateTorpedoCooldown() {
        double progress = (double) submarine.getTorpedoCooldown() / (double) game.getMapConfiguration().getTorpedoCooldown();
        torpedoCooldownBar.setProgress(progress);
    }

    private void calculateExtendedSonarCooldown() {
        double progress = (double) submarine.getSonarCooldown() / (double) game.getMapConfiguration().getExtendedSonarCooldown();
        sonarCooldownBar.setProgress(progress);
    }

    private void calculateHp() {
        double progress = (double) submarine.getHp() / originalHp;
        hpBar.setProgress(progress);
    }

}
