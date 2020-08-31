package services;

import models.TeamSelection;
import sources.DatafileIOclass;
import sources.StudentInfo;
import sources.Teams;
import utils.FileNames;
import utils.Validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FxCrudService {
    private DatafileIOclass fileIO = new DatafileIOclass();

    public void addMemberToTeam(Map<String, TeamSelection> selectionMap, String memberRow) {
        TeamSelection selection = selectionMap.values().stream().collect(Collectors.toList()).get(0);
        ArrayList<String> teamRows = fileIO.readDataFile(FileNames.TEAMS);
        String teamRow = teamRows.get(Integer.parseInt(selection.getTeamNo()) - 1);
        Teams team = Teams.fileRowToTeams(teamRow);
        StudentInfo student = StudentInfo.fileRowToStudent(memberRow);
        List<StudentInfo> studentInfos = team.getStudentInfos();
        studentInfos.set(Integer.parseInt(selection.getRowNo()) - 1, student);
        team.setStudentInfos(studentInfos);
        fileIO.updateDataFile(FileNames.TEAMS, team.getProjectId(), Teams.teamToFilerow(team));
    }

    public void swapMembersInTeams(Map<String, TeamSelection> selectionMap, List<String> memberRows) {
        List<TeamSelection> selections = selectionMap.values().stream().collect(Collectors.toList());
        ArrayList<String> teamRows = fileIO.readDataFile(FileNames.TEAMS);
        String teamRow1 = teamRows.get((Integer.parseInt(selections.get(0).getTeamNo()) - 1));
        String teamRow2 = teamRows.get((Integer.parseInt(selections.get(1).getTeamNo()) - 1));
        Teams team1 = Teams.fileRowToTeams(teamRow1);
        Teams team2 = Teams.fileRowToTeams(teamRow2);
        StudentInfo student1 = StudentInfo.fileRowToStudent(memberRows.get(0));
        StudentInfo student2 = StudentInfo.fileRowToStudent(memberRows.get(1));
        List<StudentInfo> studentInfos1 = team1.getStudentInfos();
        List<StudentInfo> studentInfos2 = team2.getStudentInfos();
        studentInfos1.set(Integer.parseInt(selections.get(0).getRowNo()) - 1, student2);
        if (teamRow1.equalsIgnoreCase(teamRow2)) {
            // swap is done in same team (team1 and team2 are equal)
            studentInfos1.set(Integer.parseInt(selections.get(1).getRowNo()) - 1, student1);
            team1.setStudentInfos(studentInfos1);
            team1.setStudentInfos(studentInfos1);
            fileIO.updateDataFile(FileNames.TEAMS, team1.getProjectId(), Teams.teamToFilerow(team1));
        } else {
            studentInfos2.set(Integer.parseInt(selections.get(1).getRowNo()) - 1, student1);
            team1.setStudentInfos(studentInfos1);
            team2.setStudentInfos(studentInfos2);
            fileIO.updateDataFile(FileNames.TEAMS, team1.getProjectId(), Teams.teamToFilerow(team1));
            fileIO.updateDataFile(FileNames.TEAMS, team2.getProjectId(), Teams.teamToFilerow(team2));
        }
    }

    public List<String> getAllMemberIdsOfTeam(Integer teamRowIndex) {
        ArrayList<String> teamRows = fileIO.readDataFile(FileNames.TEAMS);
        String teamRow = teamRows.get(teamRowIndex);
        Teams team = Teams.fileRowToTeams(teamRow);
        return team.getStudentInfos().stream().map(i -> i.getStudentId()).collect(Collectors.toList());
    }
}
