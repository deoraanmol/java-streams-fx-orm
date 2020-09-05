package services;

import hibernate.OrmTeams;
import hibernate.dao.TeamsDao;
import hibernate.services.OrmTeamsService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.TeamSelection;
import utils.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamBoxService {
    private HBox teamBoxContainer;
    private EventHandler<ActionEvent> checkboxAction;
    private FxCrudService fxCrudService;
    TeamsDao teamsDao = new TeamsDao();
    private List<OrmTeams> teamsFromDatabase;

    public TeamBoxService(HBox teamBoxContainer, EventHandler<ActionEvent> value, FxCrudService fxCrudService) {
        this.teamBoxContainer = teamBoxContainer;
        this.checkboxAction = value;
        this.fxCrudService = fxCrudService;
    }

    public TeamBoxService() {}


    public void createTeamBoxes(int size, boolean initFromDatabase) {
        this.teamsFromDatabase = teamsDao.getAllTeams().stream().limit(size).collect(Collectors.toList());
        for (Integer i=1; i<=size; i++) {
            VBox teamBox = new VBox();
            teamBox.setStyle("-fx-background-color: "+ Colors.boxColors[i-1] + "; -fx-text-fill: black");
            VBox.setMargin(this.teamBoxContainer, new Insets(10, 10, 10, 10));
            Label teamName = new Label("Team "+i);
            teamBox.getChildren().add(teamName);
            String teamNum = String.valueOf(this.teamsFromDatabase.get(i-1).getId());
            getTeamBoxFields(teamNum).forEach(p -> teamBox.getChildren().add(p));
            teamBoxContainer.getChildren().addAll(teamBox);
        }
        if (initFromDatabase) {
            this.populateMemberIdsWithOrm(size, true);
        }
    }

    public void populateMemberIdsWithOrm(int teamsToShowOnUI, boolean useCache) {
        List<OrmTeams> allTeams = useCache ? this.teamsFromDatabase : teamsDao.getAllTeams().stream().limit(teamsToShowOnUI).collect(Collectors.toList());
        this.teamsFromDatabase = allTeams;
        for (int i=0; i<teamsToShowOnUI; i++) {
            VBox teamBox = (VBox)teamBoxContainer.getChildren().get(i);
            List<String> studentIds = OrmTeamsService.getAllStudentIdsFromTeam(allTeams.get(i));
            for (Integer j=0; j<studentIds.size(); j++) {
                HBox teamField = (HBox)teamBox.getChildren().get(j+1);
                TextField textField = (TextField) teamField.getChildren().get(0);
                textField.setText(studentIds.get(j));
            }
        }
    }

    public void populateMemberIds(int teamSize) {
        for (Integer i=1; i<=teamSize; i++) {
            VBox teamBox = (VBox)teamBoxContainer.getChildren().get(i-1);
            List<String> studentIds = fxCrudService.getAllMemberIdsOfTeam(i-1);
            for (Integer j=1; j<=studentIds.size(); j++) {
                HBox teamField = (HBox)teamBox.getChildren().get(j);
                TextField textField = (TextField) teamField.getChildren().get(0);
                textField.setText(studentIds.get(j-1));
            }
        }
    }

    public List<String> getSelectedStudentIds(List<TeamSelection> teamSelections) {
        VBox teamBox1 = (VBox)teamBoxContainer.getChildren().get(Integer.parseInt(teamSelections.get(0).getTeamNo()) - 1);
        HBox teamBoxField1 = (HBox)teamBox1.getChildren().get((Integer.parseInt(teamSelections.get(0).getRowNo())));
        TextField textField1 = (TextField) teamBoxField1.getChildren().get(0);
        VBox teamBox2 = (VBox)teamBoxContainer.getChildren().get(Integer.parseInt(teamSelections.get(1).getTeamNo()) - 1);
        HBox teamBoxField2 = (HBox)teamBox2.getChildren().get((Integer.parseInt(teamSelections.get(1).getRowNo())));
        TextField textField2 = (TextField) teamBoxField2.getChildren().get(0);
        List<String> selectedIds = new ArrayList<>();
        selectedIds.add(textField1.getText());
        selectedIds.add(textField2.getText());
        return selectedIds;
    }

    private List<HBox> getTeamBoxFields(String teamNum) {
        List<HBox> fields = new ArrayList<>();
        for (Integer i=1; i<=this.teamsFromDatabase.size(); i++) {
            HBox teamBox = new HBox();
            teamBox.setPadding(new Insets(10, 10, 10, 10));
            teamBox.getChildren().addAll(getTextField(), getCheckbox(teamNum, i.toString()));
            fields.add(teamBox);
        }
        return fields;
    }

    private TextField getTextField() {
        TextField studentField = new TextField();
        studentField.setEditable(false);
        return studentField;
    }

    private CheckBox getCheckbox(String teamNum, String rowNum) {
        CheckBox checkBox = new CheckBox();
        checkBox.setId(teamNum+"|"+rowNum);
        checkBox.setOnAction(this.checkboxAction);
        checkBox.setPadding(new Insets(4, 0, 0, 4));
        return checkBox;
    }
}
