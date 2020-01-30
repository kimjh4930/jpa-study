package jpql;

import javax.persistence.*;

/**
 *  엔티티 직접 사용
 *
 *  기본키 값
 *      - JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본 키 값을 사용.
 *      - JPQL : select count(m.id) from Member m
 *               select count(m) from Member m
 *      - SQL : select count(m.id) as cnt from Member m
 *          - 두 JPQL 모두 같은 쿼리가 생성된다.
 */

public class JpaMain {

    public static void main(String args[]){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Team teamA = new Team();
            teamA.setName("teamA");

            Team teamB = new Team();
            teamB.setName("TeamB");

            Member member = new Member();
            member.setUsername("회원1");
            member.changeTeam(teamA);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();

    }
}
