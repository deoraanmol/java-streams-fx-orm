package sources;

import exceptions.EmptyListException;
import exceptions.NoLeaderException;
import exceptions.PersonalityImbalanceException;
import interfaces.ITeams;
import utils.FileNames;
import utils.Personalities;
import utils.StandardDeviationService;
import utils.Validators;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Teams implements Serializable, ITeams {
    private String projectId;
    private List<StudentInfo> students;
    public static DatafileIOclass textfileN1 = new DatafileIOclass();

    public Teams() {}
    public Teams(String id, List<StudentInfo> infos) {
        this.projectId = id;
        this.students = infos;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<StudentInfo> getStudentInfos() {
        return this.students;
    }

    public void setStudentInfos(List<StudentInfo> studentIds) {
        this.students = studentIds;
    }

    public static boolean isStudentIdExisting(String studentId) {
        DatafileIOclass fileIO = new DatafileIOclass();
        List<String> teamRows = fileIO.readDataFile(FileNames.TEAMS);
        if (teamRows == null) {
            // when there are no teams at all
            return false;
        }
        boolean isExisting = false;
        for(String teamRow: teamRows) {
            Teams team = fileRowToTeams(teamRow);
            List<String> studentIds = team.getStudentInfos().stream().map(p -> p.getStudentId()).collect(Collectors.toList());
            if(studentIds.contains(studentId)) {
                isExisting = true;
                break;
            } else {
                continue;
            }
        }
        return isExisting;
    }

    public static String teamToFilerow(Teams team) {
        List<String> row = new ArrayList();
        row.add(team.getProjectId());
        for(StudentInfo studentInfo: team.getStudentInfos()) {
            row.add(studentInfo.getStudentId());
        }
        return String.join(" ", row);
    }

    public static Teams fileRowToTeams(String row) {
        String[] columns = row.split(" ");
        Teams team = new Teams();
        team.setProjectId(columns[0]);
        List<StudentInfo> studentInfos = new ArrayList();
        int colLength = 5; // ideally, 1st col is for projectId, rest 4 for studentIds

        for(int i = 1; i < colLength; ++i) {
            String studentIdInRow = " ";
            try {
                studentIdInRow = columns[i];
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Warning: Empty column observed for Team Row: "+row+", adding blank Student ID");
            }
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStudentId(studentIdInRow);
            studentInfos.add(studentInfo);
        }

        team.setStudentInfos(studentInfos);
        return team;
    }

    public static void displayTeamPreference(List<Teams> teams){
        int i = 1;
        for (Teams team: teams) {
            System.out.println("---------%AGE OF PREFERENCES FOR TEAM: " + i + "---------------");
            System.out.println("Percentage of Students in this Team who had this Project in their first 2 preferences: " + getPercentage(team, textfileN1) + "%");
            System.out.println("------------------------");
            i++;
        }
    }

    public static void displayTeamMetrics(List<Teams> teams) {
        int i = 1;
        for (Teams team: teams) {
            System.out.println("------------------------");
            System.out.println("Average Competency Grade for Team: " + i + " is - " + getAverageCompetency(team, textfileN1));
            System.out.println("------------------------");
            i++;
        }
    }

    public static void displayTeamSkillShortage(List<Teams> teams) {
        System.out.println("---------TEAM WISE SKILL SHORTAGES---------------");
        int i = 1;
        for (Teams team: teams) {
            System.out.println("Skill Shortage of Team " + i + " is: " + getSkillShortage(team, textfileN1));
            System.out.println("------------------------");
            i++;
        }
    }

    public static Double getAverageCompetency(ITeams iTeam, DatafileIOclass fileIO) {
        Teams team = (Teams)iTeam;
        List<StudentInfo> studentInfos = team.getStudentInfos();
        Double avgStudentGradesSum = new Double(0.0D);

        Double gradeAvg;
        for(StudentInfo teamMember: studentInfos) {
            String studentRow = fileIO.fetchObjectById(FileNames.STUDENT_INFO, teamMember.getStudentId());
            Integer gradesSum = 0;
            if (!Validators.isEmpty(studentRow)) {
                StudentInfo info = StudentInfo.fileRowToStudent(studentRow);
                for (TechnicalSkill technicalSkill: info.getTechnicalSkills()) {
                    gradesSum = gradesSum + technicalSkill.getSkillGrade();
                }
                gradeAvg = new Double((double)gradesSum) / (double)info.getTechnicalSkills().size();
                avgStudentGradesSum = avgStudentGradesSum + gradeAvg;
            }
        }

        Double teamAvg = avgStudentGradesSum / (double)studentInfos.size();
        return teamAvg;
    }

    public static BigDecimal getPercentage(ITeams iTeam, DatafileIOclass fileIO) {
        Teams team = (Teams)iTeam;
        List<StudentInfo> studentInfos = team.getStudentInfos();
        List<String> studentsMatchingPreference = new ArrayList();
        for (StudentInfo studentInfo: studentInfos) {
            String preferencesRow = fileIO.fetchObjectById(FileNames.PREFERENCES, studentInfo.getStudentId());
            if (Validators.isEmpty(preferencesRow)) {
                continue;
            } else {
                Preference preference = Preference.fileRowToPreference(preferencesRow);
                List<ProjectPreference> sortedPreferences = Preference.sortByRankDesc(preference);
                int prefIndex = 1;
                for (ProjectPreference projectPreference: sortedPreferences) {
                    if (prefIndex > 2) {
                        break;
                    }
                    if (projectPreference.getProjectId().equalsIgnoreCase(team.getProjectId())) {
                        studentsMatchingPreference.add(preference.getStudentId());
                        break;
                    }
                    prefIndex++;
                }
            }
        }
        BigDecimal percentageMatched = BigDecimal.ZERO;
        if (studentInfos.size() > 0) {
            percentageMatched = (new BigDecimal(studentsMatchingPreference.size())).divide(new BigDecimal(studentInfos.size())).multiply(new BigDecimal(100));
        }
        return percentageMatched;
    }

    public static BigDecimal getSkillShortage(ITeams iTeam, DatafileIOclass fileIO) {
        Teams team = (Teams)iTeam;
        List<StudentInfo> studentInfos = team.getStudentInfos();
        List<String> studentIds = studentInfos.stream().map(i -> i.getStudentId()).collect(Collectors.toList());
        if (Validators.hasAllStringsEmpty(studentIds)) {
            return BigDecimal.ZERO;
        }
        String projectRow = fileIO.fetchObjectById(FileNames.PROJECTS, team.getProjectId());
        Project project = Project.fileRowToProject(projectRow);
        List<TechnicalSkill> expectedSkills = TechnicalSkill.sortSkillsByByNameDesc(project.getExpectedSkills());
        Map<String, BigDecimal> avgBySkillNames = new HashMap();
        Map<String, Integer> sumBySkillNames = new HashMap();
        sumBySkillNames.put("W", 0);
        sumBySkillNames.put("P", 0);
        sumBySkillNames.put("N", 0);
        sumBySkillNames.put("A", 0);

        for(StudentInfo studentInfo: studentInfos) {
            String infoRow = textfileN1.fetchObjectById(FileNames.STUDENT_INFO, studentInfo.getStudentId());
            if (!Validators.isEmpty(infoRow)) {
                StudentInfo info = StudentInfo.fileRowToStudent(infoRow);
                List<TechnicalSkill> sortedSkills = TechnicalSkill.sortSkillsByByNameDesc(info.getTechnicalSkills());
                sumBySkillNames.put("W", sumBySkillNames.get("W") + (sortedSkills.get(0)).getSkillGrade());
                sumBySkillNames.put("P", sumBySkillNames.get("P") + (sortedSkills.get(1)).getSkillGrade());
                sumBySkillNames.put("N", sumBySkillNames.get("N") + (sortedSkills.get(2)).getSkillGrade());
                sumBySkillNames.put("A", sumBySkillNames.get("A") + (sortedSkills.get(3)).getSkillGrade());
            }
        }

        avgBySkillNames.put("W", (new BigDecimal(sumBySkillNames.get("W"))).divide(new BigDecimal(expectedSkills.size())));
        avgBySkillNames.put("P", (new BigDecimal(sumBySkillNames.get("P"))).divide(new BigDecimal(expectedSkills.size())));
        avgBySkillNames.put("N", (new BigDecimal(sumBySkillNames.get("N"))).divide(new BigDecimal(expectedSkills.size())));
        avgBySkillNames.put("A", (new BigDecimal(sumBySkillNames.get("A"))).divide(new BigDecimal(expectedSkills.size())));
        BigDecimal shortageForW = (new BigDecimal((expectedSkills.get(0)).getSkillGrade())).compareTo(avgBySkillNames.get("W")) > 0 ? (new BigDecimal((expectedSkills.get(0)).getSkillGrade())).subtract(avgBySkillNames.get("W")) : new BigDecimal(0);
        BigDecimal shortageForP = (new BigDecimal((expectedSkills.get(1)).getSkillGrade())).compareTo(avgBySkillNames.get("P")) > 0 ? (new BigDecimal((expectedSkills.get(1)).getSkillGrade())).subtract(avgBySkillNames.get("P")) : new BigDecimal(0);
        BigDecimal shortageForN = (new BigDecimal((expectedSkills.get(2)).getSkillGrade())).compareTo(avgBySkillNames.get("N")) > 0 ? (new BigDecimal((expectedSkills.get(2)).getSkillGrade())).subtract(avgBySkillNames.get("N")) : new BigDecimal(0);
        BigDecimal shortageForA = (new BigDecimal((expectedSkills.get(3)).getSkillGrade())).compareTo(avgBySkillNames.get("A")) > 0 ? (new BigDecimal((expectedSkills.get(3)).getSkillGrade())).subtract(avgBySkillNames.get("A")) : new BigDecimal(0);
        BigDecimal shortage = shortageForW.add(shortageForP).add(shortageForN).add(shortageForA);
        return shortage;
    }

    public static void hasPersonalityBalance(Teams team)
            throws PersonalityImbalanceException, NoLeaderException {
        Set<String> uniquePersonalities =
                team.getStudentInfos().stream().map(p -> p.getPersonalityType()).collect(Collectors.toSet());
        if (uniquePersonalities.size() < 3) {
            throw new PersonalityImbalanceException();
        }
        if (!uniquePersonalities.contains(Personalities.LEADER.getCode())) {
            throw new NoLeaderException();
        }
    }
}
