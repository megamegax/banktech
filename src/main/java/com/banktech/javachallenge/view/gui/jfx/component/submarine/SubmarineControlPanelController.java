package com.banktech.javachallenge.view.gui.jfx.component.submarine;

import com.banktech.javachallenge.service.domain.game.Game;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.world.World;
import com.banktech.javachallenge.view.gui.jfx.component.wheel.Wheel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class SubmarineControlPanelController implements Initializable {
    @FXML
    AnchorPane wheel;

    @FXML
    Label submarineName;

    @FXML
    Slider submarineSpeedSlider;
    private World world;
    private Game game;
    private OwnSubmarine submarine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wheel.getChildren().add(new Wheel());
    }

    public SubmarineControlPanelController() {

    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setSubmarine(OwnSubmarine submarine) {
        this.submarine = submarine;
        Platform.runLater(() -> submarineName.setText(String.valueOf(submarine.getId())));
    }

    public Long getSubmarineId() {
        return submarine.getId();
    }

    public void refresh(OwnSubmarine submarine) {
        this.submarine = submarine;
    }
}
