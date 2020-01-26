package hellojpa;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

public class JpaMain {



   public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            /**
             *  select
             *         m
             *     from
             *         Member m
             *     where
             *          m.name like '%kim%' select
             *          member0_.MEMBER_ID as MEMBER_I1_6_,
             *          member0_.city as city2_6_,
             *          member0_.street as street3_6_,
             *          member0_.zipcode as zipcode4_6_,
             *          member0_.USERNAME as USERNAME5_6_
             *   from
             *       Member member0_
             *   where
             *       member0_.USERNAME like '%kim%'
             */
            List<Member> findMembers = em.createQuery(
                    "select m from Member m where m.name like '%kim%'",
                    Member.class)
                    .getResultList();

            for(Member m : findMembers){
                System.out.println("member : " + m.getName());
            }

            //Criteria
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> cqMember = em.createQuery(cq).getResultList();

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();
    }
}
