package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  JPQL 함수
 *      - JPQL에서 제공하는 함수, DB 에 관계없이 사용 할 수 있다.
 *      - CONCAT
 *      - SUBSTRING
 *      - TRIM
 *      - LOWER, UPPER
 *      - LENGTH
 *      - LOCATE
 *      - ABS, SQRT, MOD
 *      - SIZE, INDEX(JPA 용)
 *
 *  사용자 정의 함수
 *      - JPQL에서 제공하는 함수 이외에 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다.
 *
 */

public class JpaMain {

    public static void main(String args[]){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Team team = new Team();
            team.setName("teamA");

            Member member = new Member();
            member.setUsername("관리자1");
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

            // concat
            String concatQuery = "select concat('a', 'b') from Member m";
            String query = "select 'a' || 'b' from Member m"; //JPA에서는 || 도 사용 가능.
            List<String> result = em.createQuery(concatQuery, String.class)
                    .getResultList();

            for(String r : result){
                System.out.println(r);
            }

            /**
             *  사용자 정의 함수
             *  select
             *      group_concat(member0_.username) as col_0_0_
             *  from
             *      Member member0_
             */
            String customedQuery = "select function('group_concat', m.username) from Member m";

            List<String> customedResultList = em.createQuery(customedQuery, String.class)
                    .getResultList();

            for(String customed : customedResultList){
                System.out.println("c : " + customed);
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
