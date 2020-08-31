package services.charts;

import exceptions.EmptyListException;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import services.ChartsService;
import sources.DatafileIOclass;
import sources.Teams;
import utils.StandardDeviationService;

import java.util.ArrayList;
import java.util.List;

public class AvgCompChartService {
    private List<Teams> teamsList;

    public AvgCompChartService(List<Teams> teamsList) {
        this.teamsList = teamsList;
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
        barChart.setTitle("Avg Competency Levels\nStandard Deviation = "+getStdDeviation(fileIO));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for(Integer i=0; i<teamsList.size(); i++) {
            XYChart.Data<String, Number> bar = new XYChart.Data<>("Team" + (i + 1),
                    Teams.getAverageCompetency(teamsList.get(i), fileIO));
            series1.getData().add(bar);
            ChartsService.setBarColors(i, bar);
        }
        barChart.getData().addAll(series1);
        barChart.setLegendVisible(false);
        return barChart;
    }

    private String getStdDeviation(DatafileIOclass fileIO){
        StandardDeviationService service = new StandardDeviationService(fileIO);
        try {
            return service.getDeviationOfCompetency(teamsList).toString();
        } catch (EmptyListException e) {
            return "Error: No teams found";
        }
    }
}
