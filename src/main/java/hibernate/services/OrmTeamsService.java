package hibernate.services;

import hibernate.*;
import hibernate.dao.PreferenceDao;
import hibernate.dao.TeamsDao;
import interfaces.ITeams;
import models.TeamSelection;
import sources.*;
import utils.FileNames;
import utils.Validators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrmTeamsService {
    public static List<String> getAllStudentIdsFromTeam(OrmTeams team) {
        List<String> studentIds = new ArrayList<>();
        if (team != null) {
            if (team.getStudent1() != null) {
                studentIds.add(team.getStudent1().getId());
            } else {
                studentIds.add(Validators.EMPTY_STRING);
            }

            if (team.getStudent2() != null) {
                studentIds.add(team.getStudent2().getId());
            } else {
                studentIds.add(Validators.EMPTY_STRING);
            }

            if (team.getStudent3() != null) {
                studentIds.add(team.getStudent3().getId());
            } else {
                studentIds.add(Validators.EMPTY_STRING);
            }

            if (team.getStudent4() != null) {
                studentIds.add(team.getStudent4().getId());
            } else {
                studentIds.add(Validators.EMPTY_STRING);
            }
        }
        return studentIds;
    }

    public static boolean isStudentIdExisting(String studentId, TeamsDao teamsDao) {
        OrmTeams team = teamsDao.getTeamByMemberId(studentId);
        return team != null;
    }

    public static void addMemberToTeam(Map<String, TeamSelection> selectionMap, OrmStudents student, TeamsDao teamsDao) {
        TeamSelection selection = selectionMap.values().stream().collect(Collectors.toList()).get(0);
        Integer teamNumber = Integer.parseInt(selection.getTeamNo());
        Integer studentNumber = Integer.parseInt(selection.getRowNo());
        teamsDao.addStudentToTeam(teamNumber, studentNumber, student);
    }

    public static BigDecimal getPercentageForTeam(OrmTeams ormTeam, TeamsDao teamsDao) {
        List<String> studentIds = getAllStudentIdsFromTeam(ormTeam);
        List<String> studentsMatchingPreference = new ArrayList();
        PreferenceDao preferenceDao = new PreferenceDao();
        for (String studentId: studentIds) {
            OrmPreference preference = preferenceDao.getPreference(studentId);
            if (preference != null && matchPreference(preference, ormTeam)) {
                studentsMatchingPreference.add(preference.getStudentId());
            }
        }
        BigDecimal percentageMatched = BigDecimal.ZERO;
        if (studentIds.size() > 0) {
            percentageMatched = (new BigDecimal(studentsMatchingPreference.size())).divide(new BigDecimal(studentIds.size())).multiply(new BigDecimal(100));
        }
        return percentageMatched;
    }

    public static Double getAverageCompetencyForTeam(OrmTeams ormTeam, TeamsDao teamsDao) {
        List<String> studentIds = getAllStudentIdsFromTeam(ormTeam);
        Double avgStudentGradesSum = new Double(0.0D);
        Double gradeAvg;
        List<OrmStudentInfo> studentInfos = teamsDao.getStudentInfosFromTeam(ormTeam);
        for(OrmStudentInfo studentInfo: studentInfos) {
            Integer gradesSum = 0;
            OrmTechnicalSkills technicalSkill = studentInfo.getTechnicalSkills();
            int[] techSkillGrades = {technicalSkill.getpSkill(), technicalSkill.getnSkill(), technicalSkill.getaSkill(), technicalSkill.getwSkill()};
            if (!Validators.isEmpty(studentInfo)) {
                for (int skillGrade: techSkillGrades) {
                    gradesSum = gradesSum + skillGrade;
                }
                gradeAvg = new Double((double)gradesSum) / new Double(techSkillGrades.length);
                avgStudentGradesSum = avgStudentGradesSum + gradeAvg;
            }
        }

        Double teamAvg = avgStudentGradesSum / (double)studentIds.size();
        return teamAvg;
    }

    public static BigDecimal getSkillShortage(OrmTeams ormTeam, TeamsDao teamsDao) {
        List<String> studentIds = getAllStudentIdsFromTeam(ormTeam);
        List<OrmStudentInfo> studentInfos = teamsDao.getStudentInfosFromTeam(ormTeam);
        if (Validators.hasAllStringsEmpty(studentIds)) {
            return BigDecimal.ZERO;
        }
        OrmProjects ormProject = ormTeam.getProject();
        OrmTechnicalSkills expectedSkills = ormProject.getExpectedSkills();
        Map<String, BigDecimal> avgBySkillNames = new HashMap();
        Map<String, Integer> sumBySkillNames = new HashMap();
        sumBySkillNames.put("W", 0);
        sumBySkillNames.put("P", 0);
        sumBySkillNames.put("N", 0);
        sumBySkillNames.put("A", 0);

        for(OrmStudentInfo studentInfo: studentInfos) {
            if (!Validators.isEmpty(studentInfo)) {
                OrmTechnicalSkills skillsPerStudent = studentInfo.getTechnicalSkills();
                sumBySkillNames.put("W", sumBySkillNames.get("W") + skillsPerStudent.getwSkill());
                sumBySkillNames.put("P", sumBySkillNames.get("P") + skillsPerStudent.getpSkill());
                sumBySkillNames.put("N", sumBySkillNames.get("N") + skillsPerStudent.getnSkill());
                sumBySkillNames.put("A", sumBySkillNames.get("A") + skillsPerStudent.getaSkill());
            }
        }

        BigDecimal numberOfSkills = new BigDecimal(4);

        avgBySkillNames.put("W", (new BigDecimal(sumBySkillNames.get("W"))).divide(numberOfSkills));
        avgBySkillNames.put("P", (new BigDecimal(sumBySkillNames.get("P"))).divide(numberOfSkills));
        avgBySkillNames.put("N", (new BigDecimal(sumBySkillNames.get("N"))).divide(numberOfSkills));
        avgBySkillNames.put("A", (new BigDecimal(sumBySkillNames.get("A"))).divide(numberOfSkills));
        BigDecimal shortageForW = (new BigDecimal((expectedSkills.getwSkill()))).compareTo(avgBySkillNames.get("W")) > 0 ? (new BigDecimal((expectedSkills.getwSkill()))).subtract(avgBySkillNames.get("W")) : new BigDecimal(0);
        BigDecimal shortageForP = (new BigDecimal((expectedSkills.getpSkill()))).compareTo(avgBySkillNames.get("P")) > 0 ? (new BigDecimal(expectedSkills.getpSkill())).subtract(avgBySkillNames.get("P")) : new BigDecimal(0);
        BigDecimal shortageForN = (new BigDecimal((expectedSkills.getnSkill()))).compareTo(avgBySkillNames.get("N")) > 0 ? (new BigDecimal((expectedSkills.getnSkill()))).subtract(avgBySkillNames.get("N")) : new BigDecimal(0);
        BigDecimal shortageForA = (new BigDecimal((expectedSkills.getaSkill()))).compareTo(avgBySkillNames.get("A")) > 0 ? (new BigDecimal((expectedSkills.getaSkill()))).subtract(avgBySkillNames.get("A")) : new BigDecimal(0);
        BigDecimal shortage = shortageForW.add(shortageForP).add(shortageForN).add(shortageForA);
        return shortage;
    }

    public static boolean matchPreference(OrmPreference preference, OrmTeams ormTeam) {
        return (
                preference.getPreference1().equalsIgnoreCase(ormTeam.getProject().getId())
                || preference.getPreference2().equalsIgnoreCase(ormTeam.getProject().getId())
        );
    }
}
