package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  Fetch Join
 *      - SQL 에서 제공하는 JOIN 이 아님.
 *      - JPQL 에서 성능 최적화를 위해 제공하는 기능.
 *      - 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
 *      - 명령어 ::= [LEFT [OUTER] | INNER] JOIN FETCH 조인경로
 *
 *  Entity fetch join
 *      - JPQL : select m from Member m join fetch m.team
 *      - SQL : SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID = T.ID
 *          - 즉시로딩(EAGER) 으로 가져올 떄와 동일함.
 *      - fetch join : 즉시로딩을 하고싶은 것을 명시적으로 지정 할 수 있다.
 *      - 지연로딩을 적용해도, fetch join이 적용된다.
 *
 *  일대다 관계, 컬렉션 페치 조인
 *      - select t from Team t join fetch t.members where t.name = '팀A'
 *
 *  Fetch Join과 Distinct
 *      - SQL의 DISTINCT는 중복된 결과를 제거하는 명령.
 *      - JPQL에서 DISTINCT의 역할
 *          - SQL에 DISTINCT추가.
 *          - 애플리케이션에서 엔티티 중복 제거.
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

            /**
             *  select
             *      member0_.MEMBER_ID as MEMBER_I1_0_,
             *      member0_.age as age2_0_,
             *      member0_.TEAM_ID as TEAM_ID5_0_,
             *      member0_.type as type3_0_,
             *      member0_.username as username4_0_
             *  from
             *      Member member0_
             *  -> member 조회 Query가 나가고,
             * Hibernate:
             *     select
             *         team0_.TEAM_ID as TEAM_ID1_3_0_,
             *         team0_.name as name2_3_0_
             *     from
             *         Team team0_
             *     where
             *         team0_.TEAM_ID=?
             *  -> Team proxy를 채울 TeamA 에 대한 Query가 나간다.
             *
             * m : 회원1, team : teamA
             * m : 회원2, team : teamA
             * Hibernate:
             *     select
             *         team0_.TEAM_ID as TEAM_ID1_3_0_,
             *         team0_.name as name2_3_0_
             *     from
             *         Team team0_
             *     where
             *         team0_.TEAM_ID=?
             * m : 회원3, team : TeamB
             *  -> Team Proxy를 채울 TeamB에 대한 Query가 나간다.
             *
             *  쿼리가 총 3번 발생한다. => N+1 문제가 발생함.
             */
            String query = "select m from Member m";

            List<Member> members = em.createQuery(query, Member.class).getResultList();

            for(Member m : members){
                System.out.println("m : " + m.getUsername() + ", team : " + m.getTeam().getName() );
            }


            /**
             *  fetch join
             *
             *  select
             *      member0_.MEMBER_ID as MEMBER_I1_0_0_,
             *      team1_.TEAM_ID as TEAM_ID1_3_1_,
             *      member0_.age as age2_0_0_,
             *      member0_.TEAM_ID as TEAM_ID5_0_0_,
             *      member0_.type as type3_0_0_,
             *      member0_.username as username4_0_0_,
             *      team1_.name as name2_3_1_
             *  from
             *      Member member0_
             *  inner join
             *      Team team1_
             *          on member0_.TEAM_ID=team1_.TEAM_ID
             *
             *  Query 한 번으로 Member를 조회 할 때 Team 정보도 같이 가져온다.
             *  Team은 Proxy가 아님.
             *
             */
            String fetchQuery = "select m from Member m join fetch m.team";

            List<Member> fetchMembers = em.createQuery(fetchQuery, Member.class).getResultList();

            for(Member m : fetchMembers){
                System.out.println("m : " + m.getUsername() + ", team : " + m.getTeam().getName() );
            }

            //
            /**
             *  일대다 관계, 컬렉션 페치 조인.
             *
             *      team : teamA, size : 2
             *      team : teamA, size : 2
             *      team : TeamB, size : 1
             *  -> 데이터가 뻥튀기 된다. Collection은 이것을 조심해야 함.
             *
             *  distinct
             *      - SQL에서는 완전히 똑같아야 중복이 제거된다.
             *      - 그러나 JPQL에서는 엔티티가 동일하면 제거한다.
             *
             */
            String teamQuery = "select distinct t from Team t join fetch t.members";

            List<Team> teams = em.createQuery(teamQuery, Team.class).getResultList();
            for(Team t : teams){
                System.out.println("team : " + t.getName() + ", size : " + t.getMembers().size());
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
