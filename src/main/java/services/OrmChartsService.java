package services;

import hibernate.OrmTeams;
import hibernate.dao.TeamsDao;
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
import java.util.stream.Collectors;

public class OrmChartsService {
    private TeamsDao teamsDao;
    public OrmChartsService(TeamsDao teamsDao) {
        this.teamsDao = teamsDao;
    }
    public List<BarChart<String, Number>> drawCharts() {
        List<BarChart<String, Number>> charts = new ArrayList<>();
        List<OrmTeams> ormTeams = teamsDao.getAllTeams().stream().limit(5).collect(Collectors.toList());
        // 1st chart
        PrefPercentageChartService percentageChartService = new PrefPercentageChartService(null, ormTeams);
        charts.add(percentageChartService.createChart(teamsDao));
        // 2nd chart
        AvgCompChartService avgCompChartService = new AvgCompChartService(null, ormTeams);
        charts.add(avgCompChartService.createChart(teamsDao));
        // 3rd chart
        SkillGapChartService skillGapChartService = new SkillGapChartService(null, ormTeams);
        charts.add(skillGapChartService.createChart(teamsDao));
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
