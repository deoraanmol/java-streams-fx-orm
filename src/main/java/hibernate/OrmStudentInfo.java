package hibernate;

import javax.persistence.*;

@Entity
@Table(name = "student_info")
public class OrmStudentInfo {
    @Id
    private int id;

    @Column(name = "student_id")
    private String studentId;

    @OneToOne(targetEntity = OrmTechnicalSkills.class)
    @JoinColumn(name="skillset")
    private OrmTechnicalSkills technicalSkills;

    @Column(name="personality_type", unique = true)
    private String personalityType;

    @Column(name="conflict1", unique = true)
    private String conflict1;

    @Column(name="conflict2", unique = true)
    private String conflict2;

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

    public OrmTechnicalSkills getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(OrmTechnicalSkills technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public String getPersonalityType() {
        return personalityType;
    }

    public void setPersonalityType(String personalityType) {
        this.personalityType = personalityType;
    }

    public String getConflict1() {
        return conflict1;
    }

    public void setConflict1(String conflict1) {
        this.conflict1 = conflict1;
    }

    public String getConflict2() {
        return conflict2;
    }

    public void setConflict2(String conflict2) {
        this.conflict2 = conflict2;
    }
}
