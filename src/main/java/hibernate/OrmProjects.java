package hibernate;

import javax.persistence.*;

@Entity
@Table(name = "projects")
public class OrmProjects {
    @Id
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "owner")
    private String owner;

    @OneToOne(targetEntity = OrmTechnicalSkills.class)
    @JoinColumn(name="skillset", unique = true)
    private OrmTechnicalSkills expectedSkills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public OrmTechnicalSkills getExpectedSkills() {
        return expectedSkills;
    }

    public void setExpectedSkills(OrmTechnicalSkills expectedSkills) {
        this.expectedSkills = expectedSkills;
    }
}
