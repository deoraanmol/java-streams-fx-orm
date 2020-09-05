package hibernate;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class OrmStudents {
    @Id
    private String id;

    @Column(name = "skillset", nullable = false)
    private int technicalSkills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(int technicalSkills) {
        this.technicalSkills = technicalSkills;
    }
}
