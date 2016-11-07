package com.banktech.javachallenge.view.gui.jfx.component.submarine;

import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.service.world.World;
import com.banktech.javachallenge.view.domain.ViewModel;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SubmarineControlPanel extends AnchorPane {

    SubmarineControlPanelController controller;

    public SubmarineControlPanel(OwnSubmarine submarine, ViewModel model) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banktech/javachallenge/gui/jfx/component/submarine/SubmarineControlPanel.fxml"));
            Pane panel = null;
            try {
                panel = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            controller = loader.getController();
            controller.setSubmarine(submarine);
            controller.setWorld(model.getWorldMap());
            controller.setGame(model.getGame());

            this.getChildren().add(panel);
        });
    }

    public SubmarineControlPanelController getController() {
        return controller;
    }
}
