package utils;

import exceptions.EmptyListException;
import hibernate.OrmTeams;
import hibernate.dao.TeamsDao;
import hibernate.services.OrmTeamsService;
import sources.DatafileIOclass;
import sources.Teams;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrmStandardDeviationService {
    private TeamsDao teamsDao;
    private MathContext mc;

    public OrmStandardDeviationService(TeamsDao teamsDao) {
        this.teamsDao = teamsDao;
        this.mc = MathContext.DECIMAL32;
    }

    public Double getDeviationOfCompetency(List<OrmTeams> ormTeams) throws EmptyListException {
        if (ormTeams != null && ormTeams.size() > 0) {
            BigDecimal n = new BigDecimal(ormTeams.size());
//        BigDecimal n = new BigDecimal(getMockArray().size());
            List<Double> avgCompOfTeams =
                    ormTeams.stream().map(t -> OrmTeamsService.getAverageCompetencyForTeam(t, teamsDao)).collect(Collectors.toList());
            List<BigDecimal> teamCompetencies = avgCompOfTeams.stream()
                    .map(t -> t.isNaN() ? BigDecimal.ZERO : new BigDecimal(t)).collect(Collectors.toList());
//        List<Double> avgCompOfTeams = getMockArray();

            BigDecimal competenciesSum = teamCompetencies.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal mean = competenciesSum.divide(n, mc);
            return getDeviationFromMean(teamCompetencies, mean, n);
        } else {
            throw new EmptyListException("teams", FileNames.TEAMS);
        }

    }

    public Double getDeviationOfPrefPercentage(List<OrmTeams> ormTeams) throws EmptyListException {
        if (ormTeams!= null && ormTeams.size() > 0) {
            BigDecimal n = new BigDecimal(ormTeams.size());
//        BigDecimal n = new BigDecimal(getMockBigDecimals().size());
            List<BigDecimal> percentageOfTeams =
                    ormTeams.stream().map(t -> OrmTeamsService.getPercentageForTeam(t, teamsDao))
                            .collect(Collectors.toList());
//        List<BigDecimal> percentageOfTeams = getMockBigDecimals();
            BigDecimal sumOfPercentages = percentageOfTeams.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal mean = sumOfPercentages.divide(n, mc);
            return getDeviationFromMean(percentageOfTeams, mean, n);
        } else {
            throw new EmptyListException("teams", FileNames.TEAMS);
        }
    }

    public Double getDeviationOfShortfalls(List<OrmTeams> ormTeams) throws EmptyListException {
        if (ormTeams!= null && ormTeams.size() > 0) {
            BigDecimal n = new BigDecimal(ormTeams.size());
//        BigDecimal n = new BigDecimal(getMockBigDecimals().size());
            List<BigDecimal> shortageOfTeams =
                    ormTeams.stream().map(t -> OrmTeamsService.getSkillShortage(t, teamsDao)).collect(Collectors.toList());
//        List<BigDecimal> percentageOfTeams = getMockBigDecimals();
            BigDecimal sumOfShortages = shortageOfTeams.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal mean = sumOfShortages.divide(n, mc);
            return getDeviationFromMean(shortageOfTeams, mean, n);
        } else {
            throw new EmptyListException("teams", FileNames.TEAMS);
        }
    }

    private Double getDeviationFromMean(List<BigDecimal> teamValues, BigDecimal mean, BigDecimal size) {
        BigDecimal deviationSummation = new BigDecimal(0);
        for(BigDecimal teamValue: teamValues){
            BigDecimal absDiff = teamValue.subtract(mean).abs();
            absDiff = absDiff.multiply(absDiff); // square
            deviationSummation = deviationSummation.add(absDiff);
        }

        BigDecimal avgDeviation = new BigDecimal(String.valueOf(deviationSummation.divide(size, mc)));
        Double ans = Math.sqrt(avgDeviation.doubleValue());
        return ans;
    }



    public List<Double> getMockDoubles() {
        Integer[] test1 = {9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4};
        List<Double> test2 = new ArrayList<>();
        for(int i=0; i<20; i++) {
            test2.add(new Double(test1[i]));
        }
        // ans should be 8.9
        return test2;
    }

    public List<BigDecimal> getMockBigDecimals() {
        Integer[] test1 = {4, 9, 11, 12, 17, 5, 8, 12, 14};
        List<BigDecimal> test2 = new ArrayList<>();
        for(int i=0; i<test1.length; i++) {
            test2.add(new BigDecimal(test1[i]));
        }
        // ans should be 3.94
        return test2;
    }
}
