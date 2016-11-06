package com.banktech.javachallenge.view.gui.jfx;

import com.banktech.javachallenge.service.domain.game.Scores;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.domain.ApiCall;
import com.banktech.javachallenge.view.domain.ViewModel;

import com.banktech.javachallenge.view.gui.jfx.component.map.MapCanvas;
import com.banktech.javachallenge.view.gui.jfx.component.submarine.SubmarineControlPanel;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TorpedoController implements Initializable, GUIListener {

    @FXML
    VBox adminLayout;
    @FXML
    ScrollPane adminLayoutContainer;
    @FXML
    JFXToggleButton toggleAdminMode;

    @FXML
    AnchorPane mapLayout;

    @FXML
    Label turnNumber;

    @FXML
    HBox scores;

    @FXML
    JFXListView<ApiCall> logs;


    private MapCanvas canvas;
    private List<ViewModel> turns;
    private int currentIndex = 0;
    private boolean autoRefresh = true;
    private boolean submarinesCreated = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminLayout.getChildren().clear();
        canvas = new MapCanvas();
        mapLayout.getChildren().add(canvas);
        adminLayoutContainer.visibleProperty().bind(toggleAdminMode.selectedProperty());
    }

    @Override
    public void refresh(List<ViewModel> turns) {
        this.turns = turns;
        ViewModel model = turns.get(turns.size() - 1);
        if (autoRefresh) {
            canvas.setViewModel(model);
            if (model.getCalls() != null) {
                refreshLogList(model);
            }
            currentIndex = turns.size() - 1;
        }
        if (model.getGame() != null) {
            printScores(model.getGame().getScores());
            refreshTurnNumber(model);
            if (!submarinesCreated) {
                refreshAdminlayout(model);
            }
        }


    }

    private void refreshAdminlayout(ViewModel model) {
        Platform.runLater(() -> {
            for (int i = 0; i < model.getGame().getMapConfiguration().getSubmarinesPerTeam(); i++) {
                adminLayout.getChildren().add(new SubmarineControlPanel(null, model.getWorldMap()));
            }
        });
        submarinesCreated = true;
    }

    private void refreshTurnNumber(ViewModel model) {
        Platform.runLater(() -> turnNumber.setText(String.valueOf(model.getGame().getRound())));

    }

    private void printScores(Scores scoresData) {
        Platform.runLater(() -> {
            scores.getChildren().clear();
            for (Map.Entry<String, Integer> entry : scoresData.getScores().entrySet()) {
                VBox teamScore = new VBox();
                Label teamName = new Label(entry.getKey());
                teamName.setPadding(new Insets(5, 5, 5, 5));
                teamScore.getChildren().add(teamName);
                Label scoreNumber = new Label(String.valueOf(entry.getValue()));
                scoreNumber.setPadding(new Insets(5, 5, 5, 5));
                teamScore.getChildren().add(scoreNumber);
                scores.getChildren().add(teamScore);
            }
        });
    }

    @FXML
    void prevLog() {
        if ((currentIndex - 1) >= 0) {
            currentIndex--;
        }
        ViewModel model = turns.get(currentIndex);
        canvas.setViewModel(model);
        refreshLogList(model);
        autoRefresh = false;
    }

    @FXML
    void nextLog() {
        if ((currentIndex + 1) <= (turns.size() - 1)) {
            currentIndex++;
        }
        ViewModel model = turns.get(currentIndex);
        canvas.setViewModel(model);
        refreshLogList(model);
        autoRefresh = false;
    }

    @FXML
    void jumpToLastLog() {
        ViewModel model = turns.get(turns.size() - 1);
        canvas.setViewModel(model);
        refreshLogList(model);
        autoRefresh = true;
    }

    private void refreshLogList(ViewModel model) {
        Platform.runLater(() -> logs.setItems(FXCollections.observableArrayList(model.getCalls())));
    }
}
