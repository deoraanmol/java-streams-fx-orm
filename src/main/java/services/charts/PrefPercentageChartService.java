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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrefPercentageChartService {
    private List<Teams> teamsList;
    private List<OrmTeams> ormTeams;

    public PrefPercentageChartService(List<Teams> teams, List<OrmTeams> ormTeams) {
        this.teamsList = teams;
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
        yAxis.setLabel("Preference %age");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("% getting 1st & 2nd preferences\nStandard Deviation = "+getStdDeviation(fileIO));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Integer i=0; i<teamsList.size(); i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<>("Team" + (i + 1),
                    Teams.getPercentage(teamsList.get(i), fileIO));
            series1.getData().add(data);
            ChartsService.setBarColors(i, data);
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
        yAxis.setLabel("Preference %age");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("% getting 1st & 2nd preferences\nStandard Deviation = "+getStdDeviation(teamsDao));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Integer i=0; i < ormTeams.size(); i++) {
            OrmTeams team = ormTeams.get(i);
            XYChart.Data<String, Number> data = new XYChart.Data<>("Team" + team.getId(),
                    OrmTeamsService.getPercentageForTeam(team, teamsDao));
            series1.getData().add(data);
            ChartsService.setBarColors(i, data);
        }
        barChart.getData().addAll(series1);
        barChart.setLegendVisible(false);
        return barChart;
    }

    private String getStdDeviation(TeamsDao teamsDao){
        OrmStandardDeviationService service = new OrmStandardDeviationService(teamsDao);
        try {
            return service.getDeviationOfPrefPercentage(ormTeams).toString();
        } catch (EmptyListException e) {
            return "Error: No teams found";
        }
    }

    private String getStdDeviation(DatafileIOclass fileIO){
        StandardDeviationService service = new StandardDeviationService(fileIO);
        try {
            return service.getDeviationOfPrefPercentage(teamsList).toString();
        } catch (EmptyListException e) {
            return "Error: No teams found";
        }
    }
}
