package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Member member = new Member(211L, "member210");
            em.persist(member);

            em.flush();

            /**
             *  flush() 가 실행 될 때.
             *  1. em.flush() 를 직접 실행 할 때
             *  2. tx.commit() 을 실행 할 때
             *  3. JPQL 을 실행하기 전
             */

            /**
             *  flush()
             *  - 영속성 컨텍스트를 비우지 않는다.
             *  - 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
             *  - 트랜잭션 작업 단위가 중요. commit 직전에만 동기화 하면 됨.
             */

            Query query = em.createQuery("select m from Member m", Member.class);
            List<Member> findMember = query.getResultList();

            System.out.println("=======================");
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
