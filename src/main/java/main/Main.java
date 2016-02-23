package main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Child;
import model.Parent;

import org.eclipse.persistence.config.PersistenceUnitProperties;

public class Main {

    private static final int PARENT_COUNT = 1000;
    private static final int CHILD_COUNT_PER_PARENT = 9000;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");

        ddl(emf);

        for (int parentId = 0; parentId < PARENT_COUNT; ++parentId) {

            persistParent(emf, parentId);

            for (int childIndex = 0; childIndex < CHILD_COUNT_PER_PARENT; ++childIndex) {

                persistChild(emf, parentId, childIndex);

            }
        }
    }

    private static void ddl(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("drop table if exists tenant1.child").executeUpdate();
        em.createNativeQuery("drop table if exists tenant1.parent").executeUpdate();
        em.createNativeQuery("create table tenant1.parent (id int primary key, name varchar(255))").executeUpdate();
        em.createNativeQuery("create table tenant1.child (id int primary key, parent int references tenant1.parent (id))").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    private static void persistParent(EntityManagerFactory emf, Integer id) {
        EntityManager em = emf.createEntityManager();
        em.setProperty(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "tenant1");
        em.getTransaction().begin();

        Parent parent = new Parent();
        parent.setId(id);
        em.persist(parent);

        em.getTransaction().commit();
        em.close();
    }

    private static void persistChild(EntityManagerFactory emf, Integer parentId, Integer childIndex) {
        EntityManager em = emf.createEntityManager();
        em.setProperty(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "tenant1");
        em.getTransaction().begin();

        Child child = new Child();
        child.setId(parentId * CHILD_COUNT_PER_PARENT + childIndex);
        child.setParentId(parentId);
        em.persist(child);

        em.getTransaction().commit();
        em.close();
    }

}
