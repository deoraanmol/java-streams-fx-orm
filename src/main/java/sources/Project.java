package sources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Project implements Serializable {
    private String projectId;
    private String title;
    private String description;
    private String ownerId;
    private List<TechnicalSkill> expectedSkills;

    public Project() {
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<TechnicalSkill> getExpectedSkills() {
        return this.expectedSkills;
    }

    public void setExpectedSkills(List<TechnicalSkill> expectedSkills) {
        this.expectedSkills = expectedSkills;
    }

    public static Project fileRowToProject(String row) {
        String[] columns = row.split(" ");
        List<String> reversedColumns = Arrays.asList(columns);
        Collections.reverse(reversedColumns);
        Project project = new Project();
        project.setProjectId((String)reversedColumns.get(reversedColumns.size() - 1));
        List<TechnicalSkill> expectedSkills = new ArrayList();

        for(int i = 0; i < 8; i += 2) {
            TechnicalSkill skill = new TechnicalSkill();
            String grade = ((String)reversedColumns.get(i)).replaceAll("(\\r|\\n)", "");
            skill.setSkillGrade(Integer.parseInt(grade));
            skill.setSkillName((String)reversedColumns.get(i + 1));
            expectedSkills.add(skill);
        }

        project.setExpectedSkills(expectedSkills);
        return project;
    }
}
