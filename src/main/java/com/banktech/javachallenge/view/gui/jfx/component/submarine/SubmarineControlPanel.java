package com.banktech.javachallenge.view.gui.jfx.component.submarine;

import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.world.World;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class SubmarineControlPanel extends AnchorPane {


    public SubmarineControlPanel(OwnSubmarine submarine, World world) {
        System.out.println(getClass().getResource("").toString());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banktech/javachallenge/gui/jfx/component/submarine/SubmarineControlPanel.fxml"));
        Pane panel = null;
        try {
            panel = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        this.getChildren().add(panel);

    }
}
