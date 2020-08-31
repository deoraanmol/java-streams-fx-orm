package sources;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TechnicalSkill implements Serializable {
    private String skillName;
    private Integer skillGrade;
    public TechnicalSkill() {}
    public TechnicalSkill(String name, Integer grade) {
        this.skillGrade = grade;
        this.skillName = name;
    }

    public String getSkillName() {
        return this.skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getSkillGrade() {
        return this.skillGrade;
    }

    public void setSkillGrade(Integer skillGrade) {
        this.skillGrade = skillGrade;
    }

    public static List<TechnicalSkill> sortSkillsByByNameDesc(List<TechnicalSkill> list) {
        Collections.sort(list, (o1, o2) -> {
            return o2.getSkillName().compareTo(o1.getSkillName());
        });
        return list;
    }
}
