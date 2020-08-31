package sources;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TestSerializations {

    @Test
    @DisplayName("Ensure StudentInfo object is Serialised/Deserialised")
    public void testStudentInfo() throws IOException, ClassNotFoundException {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setStudentId("S1");
        studentInfo.setPersonalityType("A");
        studentInfo.setExlcudedStudentIds(Arrays.asList("S2"));
        studentInfo.setTechnicalSkills(Arrays.asList(new TechnicalSkill("W", 4)));
        byte[] object1 = Serializer.serialize(studentInfo);
        StudentInfo deserializedObj1 = (StudentInfo) Serializer.deserialize(object1);
        assertEquals(studentInfo.equals(deserializedObj1), true);
    }

    @Test
    @DisplayName("Ensure Preference object is Serialised/Deserialised")
    public void testPreference() throws IOException, ClassNotFoundException {
        Preference preference = new Preference();
        preference.setStudentId("S1");
        byte[] object1 = Serializer.serialize(preference);
        Preference deserializedObj1 = (Preference) Serializer.deserialize(object1);
        assertEquals(deserializedObj1.getStudentId(), preference.getStudentId());
    }

    @Test
    @DisplayName("Ensure ProjectPreference object is Serialised/Deserialised")
    public void testProjectPreference() throws IOException, ClassNotFoundException {
        ProjectPreference projectPreference = new ProjectPreference();
        projectPreference.setProjectId("P1");
        projectPreference.setRank(1);
        byte[] object1 = Serializer.serialize(projectPreference);
        ProjectPreference deserializedObj1 = (ProjectPreference) Serializer.deserialize(object1);
        assertEquals(deserializedObj1.getRank(), projectPreference.getRank());
        assertEquals(deserializedObj1.getProjectId(), projectPreference.getProjectId());
    }

    @Test
    @DisplayName("Ensure Project object is Serialised/Deserialised")
    public void testProject() throws IOException, ClassNotFoundException {
        Project project = new Project();
        project.setProjectId("P1");
        project.setDescription("Some desc");
        project.setOwnerId("OWN1");
        byte[] object1 = Serializer.serialize(project);
        Project deserializedObj1 = (Project) Serializer.deserialize(object1);
        assertEquals(deserializedObj1.getOwnerId(), project.getOwnerId());
        assertEquals(deserializedObj1.getDescription(), project.getDescription());
        assertEquals(deserializedObj1.getProjectId(), project.getProjectId());
    }

    @Test
    @DisplayName("Ensure Teams object is Serialised/Deserialised")
    public void testTeams() throws IOException, ClassNotFoundException {
        Teams team = new Teams();
        team.setProjectId("P1");
        team.setStudentInfos(new ArrayList<>());
        byte[] object1 = Serializer.serialize(team);
        Teams deserializedObj1 = (Teams) Serializer.deserialize(object1);
        assertEquals(deserializedObj1.getProjectId(), team.getProjectId());
        assertEquals(deserializedObj1.getStudentInfos(), team.getStudentInfos());
    }

}
