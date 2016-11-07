package com.banktech.javachallenge.view.gui.jfx;

import com.banktech.javachallenge.service.domain.game.Scores;
import com.banktech.javachallenge.service.domain.submarine.OwnSubmarine;
import com.banktech.javachallenge.view.GUIListener;
import com.banktech.javachallenge.view.domain.ApiCall;
import com.banktech.javachallenge.view.domain.ViewModel;

import com.banktech.javachallenge.view.gui.jfx.component.map.MapCanvas;
import com.banktech.javachallenge.view.gui.jfx.component.submarine.SubmarineControlPanel;
import com.banktech.javachallenge.view.gui.jfx.component.submarine.SubmarineControlPanelController;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private ArrayList<SubmarineControlPanelController> submarineControlPanels = new ArrayList<>();

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
            if (submarineControlPanels.isEmpty()) {
                addSubmarineControlPanels(model);
            }
            if (submarineControlPanels != null) {
                submarineControlPanels.forEach(submarineControlPanel -> {
                    List<OwnSubmarine> ownSubmarines = model.getOwnSubmarines();
                    if (ownSubmarines != null && !ownSubmarines.isEmpty() && submarineControlPanel != null) {
                        Long submarineId = submarineControlPanel.getSubmarineId();
                        if (submarineId != null) {
                            Optional<OwnSubmarine> submarine = ownSubmarines.stream().filter(ownSubmarine -> ownSubmarine.getId().equals(submarineId))
                                    .findFirst();

                            if (submarine.isPresent()) {
                                submarineControlPanel.refresh(submarine.get());
                            }
                        }
                    }
                });

            }
        }

    }

    private void addSubmarineControlPanels(ViewModel model) {
        model.getOwnSubmarines().stream().filter(submarine -> submarine != null).forEach(submarine -> {
            SubmarineControlPanel panel = new SubmarineControlPanel(submarine, model);
            submarineControlPanels.add(panel.getController());
            Platform.runLater(() -> adminLayout.getChildren().add(panel));
        });
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
