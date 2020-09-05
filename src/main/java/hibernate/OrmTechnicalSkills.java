package hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "technical_skills")
public class OrmTechnicalSkills {
    @Id
    private int id;

    @Column(name = "p")
    private int pSkill;

    @Column(name = "n")
    private int nSkill;

    @Column(name = "a")
    private int aSkill;

    @Column(name = "w")
    private int wSkill;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getwSkill() {
        return wSkill;
    }

    public void setwSkill(int wSkill) {
        this.wSkill = wSkill;
    }

    public int getpSkill() {
        return pSkill;
    }

    public void setpSkill(int pSkill) {
        this.pSkill = pSkill;
    }

    public int getnSkill() {
        return nSkill;
    }

    public void setnSkill(int nSkill) {
        this.nSkill = nSkill;
    }

    public int getaSkill() {
        return aSkill;
    }

    public void setaSkill(int aSkill) {
        this.aSkill = aSkill;
    }
}
