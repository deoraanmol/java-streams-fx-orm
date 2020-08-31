package sources;

import exceptions.EmptyListException;
import exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.FileNames;
import utils.StandardDeviationService;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Tests negative use-cases")
public class NegativeMileston2Tests {
    DatafileIOclass fileIO = new DatafileIOclass(EnvironmentType.TEST);

    @Test
    @DisplayName("Should throw error when no Teams are existing for Computing Std. Devation of Competency")
    void testSDForNoTeams() {
        StandardDeviationService sds = new StandardDeviationService(fileIO);
        try {
            sds.getDeviationOfCompetency(new ArrayList<>());
        } catch (EmptyListException e) {
            assertEquals("The list of teams is empty! Please ensure some data is existing in file: "+FileNames.TEAMS
                    , e.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw error when no Teams are existing for Computing Std. Devation of %age")
    void testSDPerForNoTeams() {
        StandardDeviationService sds = new StandardDeviationService(fileIO);
        try {
            sds.getDeviationOfPrefPercentage(new ArrayList<>());
        } catch (EmptyListException e) {
            assertEquals("The list of teams is empty! Please ensure some data is existing in file: "+FileNames.TEAMS
                    , e.getMessage());
        }
    }

    @Test
    @DisplayName("Should throw error when no Teams are existing for Computing Std. Devation of Shortfall")
    void testSDShortfallForNoTeams() {
        StandardDeviationService sds = new StandardDeviationService(fileIO);
        try {
            sds.getDeviationOfShortfalls(new ArrayList<>());
        } catch (EmptyListException e) {
            assertEquals("The list of teams is empty! Please ensure some data is existing in file: "+FileNames.TEAMS
                    , e.getMessage());
        }
    }

    @Test
    @DisplayName("Should not Compute Average Skill Competency for a non-existing Team Project")
    void testSkillNonExsiting() {
        String teamRow1 = fileIO.fetchObjectById("teams.txt", "pr123");
        assertEquals(null, teamRow1);
    }

    @Test
    @DisplayName("Should not Compute %age of Perference Match for non-existing Team Project")
    void testPreferenceTeamNotExisting() {
        String teamRow1 = fileIO.fetchObjectById("teams.txt", "pr123");
        assertEquals(null, teamRow1);
    }

    @Test
    @DisplayName("Should not Compute Skill Shortage for non-existing Team Project")
    void testShortageTeamNotExisting() {
        String teamRow1 = fileIO.fetchObjectById("teams.txt", "pr123");
        assertEquals(null, teamRow1);
    }

    @Test
    @DisplayName("Should throw ObjectNotFound error if non-exsiting student is being added to the Team")
    void testStudentNonExsiting() throws Exception {
        try {
            StudentInfo.addStudentToTeam("S123", new ArrayList<>());
        } catch (ObjectNotFoundException e) {
            assertEquals("Student ID: S123 does not exist in: " + FileNames.STUDENT_INFO, e.getMessage());
        }
    }
}
