package services.charts;

import exceptions.EmptyListException;
import hibernate.OrmTeams;
import hibernate.dao.TeamsDao;
import hibernate.services.OrmTeamsService;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import services.ChartsService;
import sources.DatafileIOclass;
import sources.Teams;
import utils.OrmStandardDeviationService;
import utils.StandardDeviationService;

import java.util.ArrayList;
import java.util.List;

public class SkillGapChartService {
    private List<Teams> teamsList;
    private List<OrmTeams> ormTeams;

    public SkillGapChartService(List<Teams> teamsList, List<OrmTeams> ormTeams) {
        this.teamsList = teamsList;
        this.ormTeams = ormTeams;
    }

    public BarChart<String, Number> createChart(DatafileIOclass fileIO) {
        CategoryAxis xAxis = new CategoryAxis();
        List<String> teamNames = new ArrayList<>();
        for (Integer i=0; i<teamsList.size(); i++) {
            teamNames.add("Team"+(i+1));
        }
        xAxis.setCategories(FXCollections.observableArrayList(teamNames));
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Skills Gap\nStandard Deviation = "+getStdDeviation(fileIO));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Integer i=0; i<teamsList.size(); i++) {
            XYChart.Data<String, Number> bar = new XYChart.Data<>("Team" + (i + 1),
                    Teams.getSkillShortage(teamsList.get(i), fileIO));
            series1.getData().add(bar);
            ChartsService.setBarColors(i, bar);
        }
        barChart.getData().addAll(series1);
        barChart.setLegendVisible(false);
        return barChart;
    }

    public BarChart<String, Number> createChart(TeamsDao teamsDao) {
        CategoryAxis xAxis = new CategoryAxis();
        List<String> teamNames = new ArrayList<>();
        for (Integer i=0; i < ormTeams.size(); i++) {
            teamNames.add("Team"+ormTeams.get(i).getId());
        }
        xAxis.setCategories(FXCollections.observableArrayList(teamNames));
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Skills Gap\nStandard Deviation = "+getStdDeviation(teamsDao));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Integer i=0; i < ormTeams.size(); i++) {
            XYChart.Data<String, Number> bar = new XYChart.Data<>("Team" + ormTeams.get(i).getId(),
                    OrmTeamsService.getSkillShortage(ormTeams.get(i), teamsDao));
            series1.getData().add(bar);
            ChartsService.setBarColors(i, bar);
        }
        barChart.getData().addAll(series1);
        barChart.setLegendVisible(false);
        return barChart;
    }

    private String getStdDeviation(TeamsDao teamsDao){
        OrmStandardDeviationService service = new OrmStandardDeviationService(teamsDao);
        try {
            return service.getDeviationOfShortfalls(ormTeams).toString();
        } catch (EmptyListException e) {
            return "Error: No teams found";
        }
    }

    private String getStdDeviation(DatafileIOclass fileIO){
        StandardDeviationService service = new StandardDeviationService(fileIO);
        try {
            return service.getDeviationOfShortfalls(teamsList).toString();
        } catch (EmptyListException e) {
            return "Error: No teams found";
        }
    }
}
