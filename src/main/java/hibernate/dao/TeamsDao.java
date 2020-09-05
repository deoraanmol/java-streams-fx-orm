package hibernate.dao;

import hibernate.HibernateUtil;
import hibernate.OrmStudentInfo;
import hibernate.OrmStudents;
import hibernate.OrmTeams;
import hibernate.services.OrmTeamsService;
import models.TeamSelection;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamsDao {

    public List<OrmTeams> getAllTeams() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<OrmTeams> teamsList = (List<OrmTeams>) session.createQuery(
                    "FROM OrmTeams s ORDER BY s.id ASC").list();
            return teamsList;
        } finally {
            session.close();
        }
    }

    public List<OrmTeams> getAllTeamsById(List<String> teamIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Integer> teamIdsInIntegers = teamIds.stream().map(Integer::parseInt).collect(Collectors.toList());
            Query query = session.createQuery("FROM OrmTeams s where s.id in :teamIds");
            query.setParameter("teamIds", teamIdsInIntegers);
            return query.list();
        } finally {
            session.close();
        }
    }

    public OrmTeams getTeamByMemberId(String studentId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM OrmTeams s where " +
                    "s.student1.id=:studentId or s.student2.id=:studentId or " +
                    "s.student3.id=:studentId or s.student4.id=:studentId");
            query.setParameter("studentId", studentId);
            List<OrmTeams> list = query.list();
            if (list == null || list.size() == 0) {
                return null;
            } else {
                return list.get(0);
            }
        } finally {
            session.close();
        }
    }

    public void addStudentToTeam(Integer teamNumber, Integer studentNumber, OrmStudents newStudent) {
        Session localSession = HibernateUtil.getSessionFactory().openSession();
        try {
            OrmTeams team = (OrmTeams) localSession.get(OrmTeams.class, teamNumber);
            localSession.beginTransaction();
            if (studentNumber == 1) {
                team.setStudent1(newStudent);
            } else if (studentNumber == 2) {
                team.setStudent2(newStudent);
            } else if (studentNumber == 3) {
                team.setStudent3(newStudent);
            } else if (studentNumber == 4) {
                team.setStudent4(newStudent);
            }
            localSession.getTransaction().commit();
        } finally {
            localSession.close();
        }
    }

    public List<OrmStudentInfo> getStudentInfosFromTeam(OrmTeams team) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<String> studentIds = OrmTeamsService.getAllStudentIdsFromTeam(team);
            StudentInfoDao studentInfoDao = new StudentInfoDao();
            return studentInfoDao.getStudentInfos(studentIds);
        } finally {
            session.close();
        }
    }

    public void swapMembersInTeams(Map<String, TeamSelection> selections, List<String> selectedStudentIds, List<OrmTeams> selectedTeams) {
        Session localSession = HibernateUtil.getSessionFactory().openSession();
        try {
            int idx = 1;
            for(TeamSelection selection: selections.values()) {
                localSession.beginTransaction();
                Integer teamNumber = Integer.parseInt(selection.getTeamNo());
                Integer studentNumber = Integer.parseInt(selection.getRowNo());
                OrmTeams team = localSession.get(OrmTeams.class, teamNumber);
                OrmStudents studentToBeSwapped = localSession.get(OrmStudents.class, selectedStudentIds.get(idx));
                switch (studentNumber) {
                    case 1:
                        team.setStudent1(studentToBeSwapped);
                        break;
                    case 2:
                        team.setStudent2(studentToBeSwapped);
                        break;
                    case 3:
                        team.setStudent3(studentToBeSwapped);
                        break;
                    case 4:
                        team.setStudent4(studentToBeSwapped);
                }
                localSession.getTransaction().commit();
                idx--;
            }
        } finally {
            localSession.close();
        }
    }
}
