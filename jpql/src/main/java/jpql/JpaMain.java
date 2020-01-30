package jpql;

import javax.persistence.*;

/**
 *  다형성 쿼리
 *      - 조회대상을 특정 자식으로 한정 할 수 있음
 *        ex) select i from Item i where type(i) IN (Book, Movie)
 *            -> select i from i where i.DTYPE in ('B', 'M')
 *  Treat
 *      - 자바의 타입 캐스팅과 유사
 *      - 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용
 *      - FROM, WHERE, SELECT 사용
 *        ex) select i from Item i where treat(i as Book).author = 'kim'
 *            -> select i.* from i where i.DTYPE = 'B' i.author = 'kim' // 구현전략에 따라 달라짐.
 *
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
