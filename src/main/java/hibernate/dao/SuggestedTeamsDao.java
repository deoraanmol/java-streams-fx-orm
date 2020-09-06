package hibernate.dao;

import hibernate.*;
import hibernate.services.OrmTeamsService;
import models.TeamSelection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuggestedTeamsDao {

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

    public void insertTeams(List<OrmTeams> ormTeams) {
        Session localSession = HibernateUtil.getSessionFactory().openSession();
        try {
            // clear previous suggestions
            localSession.beginTransaction();
            Query deleteQuery = localSession.createQuery("DELETE FROM OrmSuggestedTeams");
            deleteQuery.executeUpdate();
            // set new suggestions
            List<OrmSuggestedTeams> ormSuggestedTeams = OrmSuggestedTeams.fromOrmTeams(ormTeams);
            for(OrmSuggestedTeams ormSuggestedTeam: ormSuggestedTeams) {
                localSession.save("OrmSuggestedTeams", ormSuggestedTeam);
            }
            localSession.getTransaction().commit();
        } finally {
            localSession.close();
        }
    }
}
