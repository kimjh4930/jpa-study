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

            /**
             * 준영속상태. JPA에서 관리하지 않음.
             * commit 을 하더라도 DB에 반영되지 않음.
             *
             * 준영속 상태로 만드는 방법.
             *  1. em.detach(Object);
             *  2. em.clear()
             *      - 다시 조회하면 (em.find()) 다시 영속성 컨텍스트에 올라간다.
             *  3. em.close()
             */
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
