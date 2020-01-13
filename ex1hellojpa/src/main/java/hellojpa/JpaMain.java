package hellojpa;

import javax.persistence.*;

public class JpaMain {

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            //영속
            Member member = em.find(Member.class, 211L);
            member.setName("AAAAAAAA");
            em.detach(member);

            System.out.println("====================");

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
