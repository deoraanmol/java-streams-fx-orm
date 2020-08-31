package sources;

import exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.FileNames;
import utils.Personalities;
import utils.StandardDeviationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@Tag("Tests as mentioned in Miletsone2")
class Milestone2Tests {
	DatafileIOclass fileIO = new DatafileIOclass(EnvironmentType.TEST);

	@Test
	@DisplayName("Test Computed Average Skill Competency for a Team")
	void testAvgSkillCompetency() {
		String teamRow1 = fileIO.fetchObjectById("teams.txt", "pr1");
		Teams team = Teams.fileRowToTeams(teamRow1);
		Double computedAvgCompetency = Teams.getAverageCompetency(team, fileIO);
		assertEquals(new Double(2.5), computedAvgCompetency);

		String teamRow6 = fileIO.fetchObjectById("teams.txt", "pr6");
		team = Teams.fileRowToTeams(teamRow6);
		computedAvgCompetency = Teams.getAverageCompetency(team, fileIO);
		assertEquals(new Double(2.375), computedAvgCompetency);
	}

	@Test
	@DisplayName("Test Computed %age of students matching project as their first/second preference of 2 Teams")
	void testPreferencePercentage() {
		// all students have PR1 in their preference list - Hence 100%
		String teamRow1 = fileIO.fetchObjectById("teams.txt", "pr1");
		Teams team = Teams.fileRowToTeams(teamRow1);
		BigDecimal expected = Teams.getPercentage(team, fileIO);
		assertTrue(new BigDecimal(100).compareTo(expected) == 0);


		// only S1 and S20 have PR6 in their preference list - Hence 50%
		String teamRow6 = fileIO.fetchObjectById("teams.txt", "pr6");
		team = Teams.fileRowToTeams(teamRow6);
		expected = Teams.getPercentage(team, fileIO);
		assertTrue(new BigDecimal(50).compareTo(expected) == 0);
	}

	@Test
	@DisplayName("Test Skill Shortfall of 2 Teams")
	void testSkillShortage() {
		String teamRow1 = fileIO.fetchObjectById("teams.txt", "pr1");
		Teams team = Teams.fileRowToTeams(teamRow1);
		BigDecimal expected = Teams.getSkillShortage(team, fileIO);
		assertTrue(new BigDecimal(2.5).compareTo(expected) == 0);


		String teamRow2 = fileIO.fetchObjectById("teams.txt", "pr2");
		team = Teams.fileRowToTeams(teamRow2);
		expected = Teams.getSkillShortage(team, fileIO);
		assertTrue(new BigDecimal(1).compareTo(expected) == 0);
	}


	@Test
	@DisplayName("Should throw error Invalid Member Exception when a student is re-added to some other Team")
	void testFormTeams() throws Exception {
		fileIO.clearFileData("update_teams.txt");
		try {
			StudentInfo.addStudentToTeam("S1", new ArrayList<>());
		} catch (InvalidMemberException e) {
			assertEquals("Cannot add studentId: S1 since it's already added to another team", e.getMessage());
		}
		// also test if a fresh studentId is added
		List<StudentInfo> addedStudents = StudentInfo.addStudentToTeam("S10", new ArrayList<>());
		assertEquals(1, addedStudents.size());
	}

	@Test
	@DisplayName("Should throw Student Conflict Exception when a student to be added conflicts with team members")
	void testStudentConflicts() throws Exception {
		List<StudentInfo> teamMembers = new ArrayList<StudentInfo>();
		StudentInfo student1 = new StudentInfo("S1", "A", Arrays.asList("S21", "S22"));
		teamMembers.add(student1);
		try {
			StudentInfo.addStudentToTeam("S21", teamMembers);
		} catch (StudentConflictException e) {
			assertEquals("Cannot add student: S21 since it conflicts with other team members", e.getMessage());
			assertEquals(1, teamMembers.size());
		}
		// also test if a non-excluded studentId can be added
		StudentInfo.addStudentToTeam("S10", teamMembers);
		assertEquals(2, teamMembers.size());
	}

	@Test
	@DisplayName("Should throw Personality Imbalance Exception when a team has < 3 different personality types")
	void testDifferentPersonalities() throws PersonalityImbalanceException, NoLeaderException {
		List<StudentInfo> teamMembers = new ArrayList<>();
		String[] invalidPersonalities = {"A", "B", "A", "A"};
		for (int i=0; i<4; i++) {
			String studentId = "S"+(i+1);
			teamMembers.add(new StudentInfo(studentId, invalidPersonalities[i], Arrays.asList("S21", "S22")));
		}
		Teams team1 = new Teams("PR1", teamMembers);
		try {
			Teams.hasPersonalityBalance(team1);
		} catch (PersonalityImbalanceException e) {
			assertEquals("Team has Personality Imbalance, it must have at least 3 different personalities"
					, e.getMessage());
		}

		// also test if 3 different personalities are considered balanced
		String[] validPersonalities = {"A", "B", "C", "A"};
		teamMembers = new ArrayList<>();
		for (int i=0; i<4; i++) {
			String studentId = "S"+(i+1);
			teamMembers.add(new StudentInfo(studentId, validPersonalities[i], Arrays.asList("S21", "S22")));
		}
		Teams team2 = new Teams("PR2", teamMembers);
		Teams.hasPersonalityBalance(team2);
	}

	@Test
	@DisplayName("Should throw Repeated Member Exception when a StudentID already exists in the Same Team")
	void testRepeatedMembers() throws Exception {
		List<StudentInfo> teamMembers = new ArrayList<StudentInfo>();
		StudentInfo student1 = new StudentInfo("S1", "A", Arrays.asList("S21", "S22"));
		teamMembers.add(student1);
		try {
			StudentInfo.addStudentToTeam("S1", teamMembers);
		} catch (RepeatedMemberException e) {
			assertEquals("Student ID: S1 is already present in this Team", e.getMessage());
			assertEquals(1, teamMembers.size());
		}

		// also test if a unique studentId can be added to the Same Team
		StudentInfo.addStudentToTeam("S10", teamMembers);
		assertEquals(2, teamMembers.size());
	}

	@Test
	@DisplayName("Should throw No Leader Exception when a team has no Leader Personality Type")
	void testNoLeader() throws Exception {
		List<StudentInfo> teamMembers = new ArrayList<>();
		String[] personalitiesWithoutLeader = {
				Personalities.SOCIALIZER.getCode(),
				Personalities.THINKER.getCode(),
				Personalities.SUPPORTER.getCode(),
				Personalities.SUPPORTER.getCode()
		};
		for (int i=0; i<4; i++) {
			String studentId = "S"+(i+1);
			teamMembers.add(new StudentInfo(studentId, personalitiesWithoutLeader[i], Arrays.asList("S21", "S22")));
		}
		Teams team1 = new Teams("PR1", teamMembers);
		try {
			Teams.hasPersonalityBalance(team1);
		} catch (NoLeaderException e) {
			assertEquals("This Team does not have any member with Leader Type Personality", e.getMessage());
		}

		// also test if it passed when a Team has 1 Leader
		String[] validPersonalities = {
				Personalities.SOCIALIZER.getCode(),
				Personalities.THINKER.getCode(),
				Personalities.SUPPORTER.getCode(),
				Personalities.LEADER.getCode()
		};
		teamMembers = new ArrayList<>();
		for (int i=0; i<4; i++) {
			String studentId = "S"+(i+1);
			teamMembers.add(new StudentInfo(studentId, validPersonalities[i], Arrays.asList("S21", "S22")));
		}
		Teams team2 = new Teams("PR2", teamMembers);
		Teams.hasPersonalityBalance(team2);
	}


	@Test
	@DisplayName("Compute Std. Deviation in Skill Competency of all Teams correctly")
	void testSDOfSkillCompetency() throws EmptyListException {
		List<Teams> allTeams = fileIO.readDataFile(FileNames.TEAMS).stream().map(r -> Teams.fileRowToTeams(r)).collect(Collectors.toList());
		StandardDeviationService sds = new StandardDeviationService(fileIO);
		assertEquals(new Double(0.0625), sds.getDeviationOfCompetency(allTeams));
	}

	@Test
	@DisplayName("Compute Std. Deviation for percentage of project members getting first/second project preferences across projects computed correctly")
	void testSDOfPrefPercentage() throws EmptyListException {
		List<Teams> allTeams = fileIO.readDataFile(FileNames.TEAMS).stream().map(r -> Teams.fileRowToTeams(r)).collect(Collectors.toList());
		StandardDeviationService sds = new StandardDeviationService(fileIO);
		assertEquals(new Double(34.61093757759243), sds.getDeviationOfPrefPercentage(allTeams));
	}

	@Test
	@DisplayName("Compute Std. Deviation of shortfall across teams is computed correctly")
	void testSDOfShortage() throws EmptyListException {
		List<Teams> allTeams = fileIO.readDataFile(FileNames.TEAMS).stream().map(r -> Teams.fileRowToTeams(r)).collect(Collectors.toList());
		StandardDeviationService sds = new StandardDeviationService(fileIO);
		assertEquals(new Double(0.7324331368800842), sds.getDeviationOfShortfalls(allTeams));
	}

}
