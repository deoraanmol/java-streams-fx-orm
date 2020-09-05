package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.TeamSelection;
import services.ChartsService;
import services.FxCrudService;
import services.TeamBoxService;
import sources.DatafileIOclass;
import sources.Teams;
import utils.FileNames;
import utils.FxErrorMessages;
import utils.Validators;

import java.util.*;
import java.util.stream.Collectors;

public class FxController {

    DatafileIOclass fileIO = new DatafileIOclass();
    FxCrudService fxCrudService = new FxCrudService();
    @FXML
    public HBox chartsContainer;
    @FXML
    public HBox teamBoxContainer;
    @FXML
    public TextField studentInput;
    @FXML
    Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    Alert success = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    public Map<String, TeamSelection> selection = new HashMap<>();
    private TeamBoxService teamBoxesService;
    private ChartsService chartsService;


    public void addMemberToTeam(ActionEvent actionEvent) {
        String studentId = this.studentInput.getText();
        if (Validators.isEmpty(studentId)) {
            alert.setContentText(FxErrorMessages.INVALID_STUDENT_ID);
            alert.show();
        } else if (Validators.isInvalidSelectionForAddition(this.selection)) {
            alert.setContentText(FxErrorMessages.INVALID_ADDITION_CHECKBOX);
            alert.show();
        } else {
            String studentRow = fileIO.fetchObjectById(FileNames.STUDENT_INFO, studentId);
            if (Validators.isEmpty(studentRow)) {
                alert.setContentText(FxErrorMessages.STUDENT_NON_EXISTING);
                alert.show();
            } else if (Teams.isStudentIdExisting(studentId)) {
                alert.setContentText(FxErrorMessages.STUDENT_ALREADY_ADDED);
                alert.show();
            } else {
                // todo - check student is already added to other team?
                // todo - handle empty slots
                // actual member addition to team
                fxCrudService.addMemberToTeam(this.selection, studentRow);
                // update values in UI, after teams.txt is updated above
                updateTextFields(FxErrorMessages.STUDENT_ADDED);
                drawCharts();
            }
        }
    }

    public void swapMembers(ActionEvent actionEvent) {
        if (Validators.isInvalidSelectionForSwap(this.selection)) {
            alert.setContentText(FxErrorMessages.INVALID_SWAP_CHECKBOX);
            alert.show();
        } else {
            List<String> selectedStudentIds = teamBoxesService.getSelectedStudentIds(this.selection.values().stream().collect(Collectors.toList()));
            List<String> studentRows = selectedStudentIds.stream().map(id -> fileIO.fetchObjectById(FileNames.STUDENT_INFO, id)).collect(Collectors.toList());
            if (Validators.isAnyStringEmpty(studentRows)) {
                alert.setContentText(FxErrorMessages.ANY_STUDENT_NON_EXISTING);
                alert.show();
            } else {
                fxCrudService.swapMembersInTeams(this.selection, studentRows);
                drawCharts();
                updateTextFields(FxErrorMessages.STUDENTS_SWAPPED);
            }
        }
    }

    public void updateSelection(ActionEvent actionEvent) {
        boolean isSelected = ((CheckBox) actionEvent.getTarget()).isSelected();
        String sourceId = ((CheckBox) actionEvent.getSource()).getId();
        String[] sources = ((CheckBox) actionEvent.getSource()).getId().split("|");
        if (isSelected) {
            this.selection.put(sourceId, new TeamSelection(sources[0], sources[2]));
        } else {
            this.selection.remove(sourceId);
        }
    }

    @FXML
    public void initialize() {
        studentInput.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getText().trim();
            change.setText(text.toUpperCase());
            return change;
        }));
        createTeamBoxes();
        drawCharts();
    }

    private void createTeamBoxes() {
        TeamBoxService teamBoxService =
                new TeamBoxService(teamBoxContainer, this::updateSelection, fxCrudService);
        teamBoxService.createTeamBoxes(5, true);
        teamBoxesService = teamBoxService;
    }

    private void drawCharts() {
        List<String> teamRows = fileIO.readDataFile(FileNames.TEAMS);
        List<Teams> teams = teamRows.stream().map(i -> Teams.fileRowToTeams(i)).collect(Collectors.toList());
        chartsService = new ChartsService(teams, fileIO);
        chartsContainer.getChildren().setAll(chartsService.drawCharts());
    }

    private void updateTextFields(String successMessage) {
        teamBoxesService.populateMemberIds(5);
        success.setContentText(successMessage);
        success.show();
    }
}
