package hibernate.dao;

import hibernate.HibernateUtil;
import hibernate.OrmStudentInfo;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StudentInfoDao {
    public List<OrmStudentInfo> getStudentInfos(List<String> studentIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createQuery("FROM OrmStudentInfo s where s.studentId in :studentIds");
            query.setParameter("studentIds", studentIds);
            return query.list();
        } finally {
            session.close();
        }
    }
}
