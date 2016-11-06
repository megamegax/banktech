package com.banktech.javachallenge.view.gui.jfx.component.submarine;

import com.banktech.javachallenge.view.gui.jfx.component.wheel.Wheel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;


public class SubmarineControlPanelController implements Initializable {
    @FXML
    AnchorPane wheel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wheel.getChildren().add(new Wheel());
    }

    public SubmarineControlPanelController() {

    }

}
