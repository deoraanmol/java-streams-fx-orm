package services;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import services.charts.AvgCompChartService;
import services.charts.PrefPercentageChartService;
import services.charts.SkillGapChartService;
import sources.DatafileIOclass;
import sources.Teams;
import utils.Colors;

import java.util.ArrayList;
import java.util.List;

public class ChartsService {
    private List<Teams> teamsList;
    private DatafileIOclass fileIO;
    public ChartsService(List<Teams> teamsList, DatafileIOclass fileIO) {
        this.teamsList = teamsList;
        this.fileIO = fileIO;
    }
    public List<BarChart<String, Number>> drawCharts() {
        List<BarChart<String, Number>> charts = new ArrayList<>();
        // 1st chart
        PrefPercentageChartService percentageChartService = new PrefPercentageChartService(teamsList, null);
        charts.add(percentageChartService.createChart(fileIO));
        // 2nd chart
        AvgCompChartService avgCompChartService = new AvgCompChartService(teamsList, null);
        charts.add(avgCompChartService.createChart(fileIO));
        // 3rd chart
        SkillGapChartService skillGapChartService = new SkillGapChartService(teamsList, null);
        charts.add(skillGapChartService.createChart(fileIO));
        return charts;
    }

    public static void setBarColors(int idx, XYChart.Data<String, Number> bar) {
        Integer finalI = idx;
        // need to add a listener since node addition is async here
        bar.nodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setStyle("-fx-background-color: "+ Colors.boxColors[finalI] + "; -fx-text-fill: black");
            }
        });
    }
}
