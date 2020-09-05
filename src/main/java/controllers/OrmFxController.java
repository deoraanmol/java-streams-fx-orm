package controllers;

import hibernate.OrmStudents;
import hibernate.OrmTeams;
import hibernate.dao.StudentsDao;
import hibernate.dao.TeamsDao;
import hibernate.services.OrmTeamsService;
import interfaces.ITeams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import models.TeamSelection;
import services.ChartsService;
import services.FxCrudService;
import services.OrmChartsService;
import services.TeamBoxService;
import sources.DatafileIOclass;
import sources.Teams;
import utils.FileNames;
import utils.FxErrorMessages;
import utils.Validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrmFxController {

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
    private OrmChartsService ormChartsService;
    StudentsDao studentsDao = new StudentsDao();
    TeamsDao teamsDao = new TeamsDao();


    public void addMemberToTeam(ActionEvent actionEvent) {
        String studentId = this.studentInput.getText();
        if (Validators.isEmpty(studentId)) {
            alert.setContentText(FxErrorMessages.INVALID_STUDENT_ID);
            alert.show();
        } else if (Validators.isInvalidSelectionForAddition(this.selection)) {
            alert.setContentText(FxErrorMessages.INVALID_ADDITION_CHECKBOX);
            alert.show();
        } else {
            OrmStudents student = studentsDao.getStudentById(studentId);
            if (Validators.isEmpty(student)) {
                alert.setContentText(FxErrorMessages.STUDENT_NON_EXISTING);
                alert.show();
            } else if (OrmTeamsService.isStudentIdExisting(studentId, teamsDao)) {
                alert.setContentText(FxErrorMessages.STUDENT_ALREADY_ADDED);
                alert.show();
            } else {
                // actual member addition to teams table in database
                OrmTeamsService.addMemberToTeam(this.selection, student, teamsDao);
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
            List<OrmStudents> ormStudents = studentsDao.getAllStudentsById(selectedStudentIds);
            List<String> selectedTeamIds = this.selection.values().stream().map(TeamSelection::getTeamNo).collect(Collectors.toList());
            List<OrmTeams> ormTeams = teamsDao.getAllTeamsById(selectedTeamIds);
            if (ormStudents != null && ormStudents.size() == 2) {
                teamsDao.swapMembersInTeams(this.selection, selectedStudentIds, ormTeams);
                drawCharts();
                updateTextFields(FxErrorMessages.STUDENTS_SWAPPED);
            } else {
                alert.setContentText(FxErrorMessages.INVALID_STUDENTS_SELECTED);
                alert.show();
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
        ormChartsService = new OrmChartsService(teamsDao);
        chartsContainer.getChildren().setAll(ormChartsService.drawCharts());
    }

    private void updateTextFields(String successMessage) {
        teamBoxesService.populateMemberIdsWithOrm(5, false);
        success.setContentText(successMessage);
        success.show();
    }
}
