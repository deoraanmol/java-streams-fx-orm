package hibernate;

import interfaces.ITeams;

import javax.persistence.*;

@Entity
@Table(name = "project_preference")
public class OrmPreference implements ITeams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name = "preferences_string", nullable = false, unique = true)
    private String preferncesString;

    @Column(name = "preference1", nullable = false, unique = true)
    private String preference1;

    @Column(name = "preference2", nullable = false, unique = true)
    private String preference2;

    public String getPreference1() {
        return preference1;
    }

    public void setPreference1(String preference1) {
        this.preference1 = preference1;
    }

    public String getPreference2() {
        return preference2;
    }

    public void setPreference2(String preference2) {
        this.preference2 = preference2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPreferncesString() {
        return preferncesString;
    }

    public void setPreferncesString(String preferncesString) {
        this.preferncesString = preferncesString;
    }
}
