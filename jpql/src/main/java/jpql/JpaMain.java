package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  Named Query
 *
 *  정적쿼리
 *      - 미리 정의해서 이름을 부여해두고 자용하는 JPQL
 *      - 어노테이션, xml에 정의
 *      - 애플리케이션 로딩 시점에 초기화 한 후 재사용 -> 중요
 *      - 애플리케이션 로딩 시점에 쿼리를 검증. -> 중요
 *
 *  XML에 정의
 *      - XML이 항상 우선권을 가진다.
 *      - 애플리케이션 운영 환경에 따라 다른 XML을 배포 할 수 있다.
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

            List<Member > result = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for(Member m : result){
                System.out.println("member : " + m);
            }

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();

    }
}
