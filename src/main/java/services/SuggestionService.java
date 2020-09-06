package services;

import enums.SuggestionMode;
import hibernate.OrmPreference;
import hibernate.OrmStudents;
import hibernate.OrmTeams;
import hibernate.dao.PreferenceDao;
import hibernate.dao.SuggestedTeamsDao;
import hibernate.dao.TeamsDao;
import hibernate.services.OrmTeamsService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class SuggestionService {
    /*
    Algo for Heuristic Completion which lowers the Standard Devation of all charts
    1. It saves the suggestion state in `suggested_teams` table, from which we show/dont show the records depending on user's selection.
     */
    public static void suggestTeams(TeamsDao teamsDao, int size) {
        List<OrmTeams> allTeams = teamsDao.getAllTeams(SuggestionMode.DISABLED).stream().limit(size).collect(Collectors.toList());

        Map<BigDecimal, List<OrmTeams>> teamsByPercentages = new HashMap<>();
        for (OrmTeams team : allTeams) {
            BigDecimal percentage = OrmTeamsService.getPercentageForTeam(team, teamsDao);
            if (teamsByPercentages.get(percentage) != null) {
                teamsByPercentages.get(percentage).add(team);
            } else {
                List<OrmTeams> percTeams = new ArrayList<>();
                percTeams.add(team);
                teamsByPercentages.put(percentage, percTeams);
            }
        }
        Set<BigDecimal> percentages = teamsByPercentages.keySet();
        BigDecimal minPercentage = percentages.stream().min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        List<OrmTeams> minimumPercentageTeams = teamsByPercentages.get(minPercentage); // 25 percent
        List<OrmTeams> heavyWeightTeams = new ArrayList<>(); // 75 percent
        for(BigDecimal key: percentages) {
            if (minPercentage.compareTo(key) != 0) {
                heavyWeightTeams.addAll(teamsByPercentages.get(key));
            }
        }
        for(OrmTeams heavyTeam: heavyWeightTeams) {
            // diff is 75-25= 50
            BigDecimal diff = OrmTeamsService.getPercentageForTeam(heavyTeam, teamsDao).subtract(minPercentage);
            int numberOfSwappings = numberOfSwappings(diff);
            List<String> studentsAddedToLightTeam = new ArrayList<>();
            for (int i=0; i<numberOfSwappings; i++) {
                OrmTeams minPercTeam = minimumPercentageTeams.get(i);
                List<String> heavyWeightStudents = getMatchingStudentIds(heavyTeam);
                for(String heavyStudentId: heavyWeightStudents) {
                    if(!isStudentMatching(heavyStudentId, minPercTeam)) {
                        List<String> lightWeightStudents = getNonMatchingStudentIds(minPercTeam, studentsAddedToLightTeam);
                        for(String lightStudentId: lightWeightStudents) {
                            if(!isStudentMatching(lightStudentId, heavyTeam)) {
                                Map<Integer, OrmStudents> lightStudent = findStudentNumber(minPercTeam, lightStudentId);
                                Map<Integer, OrmStudents> heavyStudent = findStudentNumber(heavyTeam, heavyStudentId);
                                setStudentWithNumber((Integer) heavyStudent.keySet().toArray()[0], (OrmStudents)lightStudent.values().toArray()[0], heavyTeam);
                                studentsAddedToLightTeam.add(((OrmStudents)heavyStudent.values().toArray()[0]).getId());
                                setStudentWithNumber((Integer) lightStudent.keySet().toArray()[0], (OrmStudents)heavyStudent.values().toArray()[0], minPercTeam);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        SuggestedTeamsDao suggestedTeamsDao = new SuggestedTeamsDao();
        suggestedTeamsDao.insertTeams(allTeams);
    }

    public static void setStudentWithNumber(Integer studentNumber, OrmStudents newStudent, OrmTeams team) {
        switch (studentNumber) {
            case 1:
                team.setStudent1(newStudent);
                break;
            case 2:
                team.setStudent2(newStudent);
                break;
            case 3:
                team.setStudent3(newStudent);
                break;
            case 4:
                team.setStudent4(newStudent);
        }
    }

    public static Map<Integer, OrmStudents> findStudentNumber(OrmTeams team, String studentId) {
        HashMap<Integer, OrmStudents> map = new HashMap<>();
        if(team.getStudent1().getId().equalsIgnoreCase(studentId)) {
            map.put(1, team.getStudent1());
        } else if(team.getStudent2().getId().equalsIgnoreCase(studentId)) {
            map.put(2, team.getStudent2());
        } else if(team.getStudent3().getId().equalsIgnoreCase(studentId)) {
            map.put(3, team.getStudent3());
        } else if(team.getStudent4().getId().equalsIgnoreCase(studentId)) {
            map.put(4, team.getStudent4());
        }
        return map;
    }

    public static int numberOfSwappings(BigDecimal diff) {
        Integer diffInInteger = new Integer(diff.intValue());
        switch (diffInInteger) {
            case 25:
                return 1;
            case 50:
                return 2;
            case 75:
                return 3;
            default:
                return 0;
        }
    }

    public static List<String> getNonMatchingStudentIds(OrmTeams team, List<String> studentsJustAdded) {
        List<String> studentIds = OrmTeamsService.getAllStudentIdsFromTeam(team);
        String projectId = team.getProject().getId();
        PreferenceDao preferenceDao = new PreferenceDao();
        List<OrmPreference> projectPreferenceList = preferenceDao.getPreferenceListFromStudentIds(studentIds);
        List<String> nonMatchedStudentIds = new ArrayList<>();
        for(OrmPreference preference: projectPreferenceList) {
            if(!preference.getPreference1().equalsIgnoreCase(projectId)
                    && !preference.getPreference2().equalsIgnoreCase(projectId)
                    && !(studentsJustAdded.contains(preference.getStudentId()))) {
                nonMatchedStudentIds.add(preference.getStudentId());
            }
        }
        return nonMatchedStudentIds;
    }

    public static List<String> getMatchingStudentIds(OrmTeams team) {
        List<String> studentIds = OrmTeamsService.getAllStudentIdsFromTeam(team);
        String projectId = team.getProject().getId();
        PreferenceDao preferenceDao = new PreferenceDao();
        List<OrmPreference> projectPreferenceList = preferenceDao.getPreferenceListFromStudentIds(studentIds);
        List<String> matchedStudentIds = new ArrayList<>();
        for(OrmPreference preference: projectPreferenceList) {
            if(preference.getPreference1().equalsIgnoreCase(projectId)
                    || preference.getPreference2().equalsIgnoreCase(projectId)) {
                matchedStudentIds.add(preference.getStudentId());
            }
        }
        return matchedStudentIds;
    }

    public static boolean isStudentMatching(String studentId, OrmTeams lightTeam) {
        PreferenceDao preferenceDao = new PreferenceDao();
        OrmPreference preference = preferenceDao.getPreference(studentId);
        if(preference.getPreference1().equalsIgnoreCase(lightTeam.getProject().getId())
                || preference.getPreference2().equalsIgnoreCase(lightTeam.getProject().getId())) {
            return true;
        }
        return false;
    }
}
