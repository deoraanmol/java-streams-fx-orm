package sources;

import exceptions.*;
import utils.FileNames;
import utils.FileUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StudentInfo implements Serializable {
    public static DatafileIOclass fileIO = new DatafileIOclass();
    private String studentId;
    private List<TechnicalSkill> technicalSkills;
    private String personalityType;
    private List<String> exlcudedStudentIds;

    public StudentInfo() {
    }
    public StudentInfo(String studentId, String personalityType, List<String> exlcudedStudentIds) {
        this.studentId = studentId;
        this.personalityType = personalityType;
        this.exlcudedStudentIds = exlcudedStudentIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentInfo that = (StudentInfo) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(personalityType, that.personalityType) &&
                Objects.equals(exlcudedStudentIds, that.exlcudedStudentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, technicalSkills, personalityType, exlcudedStudentIds);
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<TechnicalSkill> getTechnicalSkills() {
        return this.technicalSkills;
    }

    public void setTechnicalSkills(List<TechnicalSkill> technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public String getPersonalityType() {
        return this.personalityType;
    }

    public void setPersonalityType(String personalityType) {
        this.personalityType = personalityType;
    }

    public List<String> getExlcudedStudentIds() {
        return this.exlcudedStudentIds;
    }

    public void setExlcudedStudentIds(List<String> exlcudedStudentIds) {
        this.exlcudedStudentIds = exlcudedStudentIds;
    }

    public static StudentInfo fileRowToStudent(String fileRow) {
        String[] columns = fileRow.split(" ");
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setStudentId(columns[0]);
        int i = 1;

        ArrayList skills;
        for(skills = new ArrayList(); i < 9; i += 2) {
            TechnicalSkill technicalSkill = new TechnicalSkill();
            technicalSkill.setSkillName(columns[i]);
            technicalSkill.setSkillGrade(Integer.parseInt(columns[i + 1]));
            skills.add(technicalSkill);
        }

        studentInfo.setPersonalityType(columns[9]);
        studentInfo.setTechnicalSkills(skills);
        List<String> excludedStudentIds = new ArrayList();
        excludedStudentIds.add(columns[10]);
        excludedStudentIds.add(FileUtils.clearSpaces(columns[11]));
        studentInfo.setExlcudedStudentIds(excludedStudentIds);
        return studentInfo;
    }

    public static String studentToFilerow(StudentInfo studentInfo) {
        String[] columns = new String[12];
        columns[0] = studentInfo.getStudentId();
        int i = 1;

        for(Iterator var3 = studentInfo.getTechnicalSkills().iterator(); var3.hasNext(); i += 2) {
            TechnicalSkill technicalSkill = (TechnicalSkill)var3.next();
            columns[i] = technicalSkill.getSkillName();
            columns[i + 1] = technicalSkill.getSkillGrade().toString();
        }

        int j = 10;

        for(Iterator var7 = studentInfo.getExlcudedStudentIds().iterator(); var7.hasNext(); ++j) {
            String excludedId = (String)var7.next();
            columns[j] = excludedId;
        }

        String row = String.join(" ", columns);
        return row;
    }

    public static List<StudentInfo> addStudentToTeam(String studentId, List<StudentInfo> teamMembers) throws Exception {
        isMemberRepeatedInTeam(studentId, teamMembers); // may throw RepeatedMemberException
        boolean isStudentIdAlreadyAdded = Teams.isStudentIdExisting(studentId);
        List<String> studentIdsWithConflicts = new ArrayList();
        List<String> addedPersonalities = new ArrayList();
        for(StudentInfo student: teamMembers) {
            studentIdsWithConflicts.addAll(student.getExlcudedStudentIds());
            addedPersonalities.add(student.getPersonalityType());
        }
        if (isStudentIdAlreadyAdded) {
            throw new InvalidMemberException(studentId);
        } else {
            if (studentIdsWithConflicts.contains(studentId)) {
                // throw error saying S1 cannot be added since it conflicts with S2
                throw new StudentConflictException(studentId);
            } else {
                String row = fileIO.fetchObjectById(FileNames.STUDENT_INFO, studentId);
                if (row == null) {
                    throw new ObjectNotFoundException("Student", studentId, FileNames.STUDENT_INFO);
                }
                StudentInfo studentInfo1 = StudentInfo.fileRowToStudent(row);
                studentIdsWithConflicts.addAll(studentInfo1.getExlcudedStudentIds());
                if (addedPersonalities.contains(studentInfo1.getPersonalityType())) {
                    throw new InvalidMemberException(studentId, "another student with same Personality Type is already added");
                } else {
                    addedPersonalities.add(studentInfo1.getPersonalityType());
                    teamMembers.add(studentInfo1);
                }
            }
        }
        return teamMembers;
    }

    public static void isMemberRepeatedInTeam(String studentId, List<StudentInfo> teamMembers) throws RepeatedMemberException {
        List<String> membersIds = teamMembers.stream().map(t -> t.getStudentId()).collect(Collectors.toList());
        if (membersIds.contains(studentId)) {
            throw new RepeatedMemberException(studentId);
        }
    }
}
