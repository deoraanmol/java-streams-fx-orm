package sources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Preference implements Serializable {
    private String studentId;
    private List<ProjectPreference> projectPreferences;

    public Preference() {
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<ProjectPreference> getProjectPreferences() {
        return this.projectPreferences;
    }

    public void setProjectPreferences(List<ProjectPreference> projectPreferences) {
        this.projectPreferences = projectPreferences;
    }

    public static List<ProjectPreference> sortByRankDesc(Preference preference) {
        List<ProjectPreference> list = preference.getProjectPreferences();
        Collections.sort(list, (o1, o2) -> o2.getRank().compareTo(o1.getRank()));
        return list;
    }

    public static Preference fileRowToPreference(String row) {
        String[] columns = row.split(" ");
        Preference preference = new Preference();
        preference.setStudentId(columns[0]);
        List<ProjectPreference> projectPreferences = new ArrayList();

        for(int i = 1; i < columns.length; i += 2) {
            ProjectPreference projectPreference = new ProjectPreference();
            projectPreference.setProjectId(columns[i]);
            columns[i + 1] = columns[i + 1].replaceAll("(\\r|\\n)", "");
            projectPreference.setRank(Integer.parseInt(columns[i + 1]));
            projectPreferences.add(projectPreference);
        }

        preference.setProjectPreferences(projectPreferences);
        return preference;
    }
}
