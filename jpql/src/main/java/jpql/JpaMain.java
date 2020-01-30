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
 *
 *  페치 조인의 특징과 한계
 *      - fetch join 대상에는 별칭을 줄 수 없다.
 *        ex) "select t from Team t join fetch t.member m" -> 관례상 금지.
 *          - fetch join 은 기본적으로 나와 연관된 것을 모두 가져오겠다는 것
 *          - 하이버네이트는 가능, 가급적 사용하지 말 것.
 *      - 둘 이상의 컬렉션은 페치 조인을 할 수 없다.
 *      - 컬렉션을 페치조인 하면 페이징 API를 사용 할 수 없다.
 *          - 일대다 -> 데이터가 뻥튀기 된다
 *            뻥튀기가 된 상태로 같은 fetch join과 연관된 데이터의 일부가 페이지 중간에 잘리면
 *            데이터 개수에 왜곡이 발생.
 *          - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능.
 *          - 하이버네이트는 경고 로그를 남기고 메모리에서 페이징 (매우 위험하다.)
 *              - WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
 *          - 해결방법
 *              - 일대다 쿼리를 다대일로 Query방향을 바꾼다.
 *                ex) select m from Member m join fetch m.team t
 *      - 연관된 엔티티들을 SQL 한 번으로 조회 -> 성능 최적화
 *      - 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선
 *      - 실무에서 글로벌 로딩 전략은 모두 지연로딩
 *      - 최적화가 필요한 곳은 fetch join 적
 *
 *  정리
 *      - 모든 것을 fetch join으로 해결 할 수 없음.
 *      - 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
 *      - 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야한다면,
 *        페치조인보다는 일반 조인을 사용하고 필요한 데이터만 조회해서 DTO로 변환하는 것이 효과적.
 *          - Entity를 조회해서 그대로 사용
 *          - fetch join 을 사용해서 Application에서 DTO로 변환한다.
 *          - DTO로 변환해서 조회한다.
 *
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

            /**
             *  WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
             *  메모리에서 페이징을 한다.
             *
             *  select
             *      team0_.TEAM_ID as TEAM_ID1_3_0_,
             *      members1_.MEMBER_ID as MEMBER_I1_0_1_,
             *      team0_.name as name2_3_0_,
             *      members1_.age as age2_0_1_,
             *      members1_.TEAM_ID as TEAM_ID5_0_1_,
             *      members1_.type as type3_0_1_,
             *      members1_.username as username4_0_1_,
             *      members1_.TEAM_ID as TEAM_ID5_0_0__,
             *      members1_.MEMBER_ID as MEMBER_I1_0_0__
             *  from
             *      Team team0_
             *  inner join
             *      Member members1_
             *          on team0_.TEAM_ID=members1_.TEAM_ID
             *
             *  Paging Query가 나가지 않는다. -> 메모리상에서 Paging, 매우 위험하다.
             */
            String fetchErrorQuery = "select t From Team t join fetch t.members m";
            //String fetchQuery = "select m from Member m join fetch m.team t"  // fetch join 방향을 바꾼다.
            List<Team> result = em.createQuery(fetchErrorQuery, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for(Team t : result){
                System.out.println("team : " + t.getName() + ", size : " + t.getMembers().size());
            }

            /**
             *  위 문제 해결방법
             *
             *  select
             *             team0_.TEAM_ID as TEAM_ID1_3_,
             *             team0_.name as name2_3_
             *         from
             *             Team team0_ limit ?
             * Hibernate:
             *     select
             *         members0_.TEAM_ID as TEAM_ID5_0_0_,
             *         members0_.MEMBER_ID as MEMBER_I1_0_0_,
             *         members0_.MEMBER_ID as MEMBER_I1_0_1_,
             *         members0_.age as age2_0_1_,
             *         members0_.TEAM_ID as TEAM_ID5_0_1_,
             *         members0_.type as type3_0_1_,
             *         members0_.username as username4_0_1_
             *     from
             *         Member members0_
             *     where
             *         members0_.TEAM_ID=?
             * team : teamA, size : 2
             * 	Member = Member{id=1, username='회원1', age=0}
             * 	Member = Member{id=3, username='회원2', age=0}
             * Hibernate:
             *     select
             *         members0_.TEAM_ID as TEAM_ID5_0_0_,
             *         members0_.MEMBER_ID as MEMBER_I1_0_0_,
             *         members0_.MEMBER_ID as MEMBER_I1_0_1_,
             *         members0_.age as age2_0_1_,
             *         members0_.TEAM_ID as TEAM_ID5_0_1_,
             *         members0_.type as type3_0_1_,
             *         members0_.username as username4_0_1_
             *     from
             *         Member members0_
             *     where
             *         members0_.TEAM_ID=?
             * team : TeamB, size : 1
             * 	Member = Member{id=4, username='회원3', age=0}
             *
             * 	=>  Query를 확인하면 Lazy로딩으로 N+1 문제가 발생함.
             * 	    Fetch Join을 쓰면 좋겠지만, Paging API와 같이 사용 할 수 없다.
             *
             * 	select
             *         members0_.TEAM_ID as TEAM_ID5_0_1_,
             *         members0_.MEMBER_ID as MEMBER_I1_0_1_,
             *         members0_.MEMBER_ID as MEMBER_I1_0_0_,
             *         members0_.age as age2_0_0_,
             *         members0_.TEAM_ID as TEAM_ID5_0_0_,
             *         members0_.type as type3_0_0_,
             *         members0_.username as username4_0_0_
             *     from
             *         Member members0_
             *     where
             *         members0_.TEAM_ID in (
             *             ?, ? => TeamA, TeamB가 들어감.
             *         }
             *   @BatchSize(100) 을 설정하거나, persistence.xml에 전역변수로 명시한다.
             *   InQuery로 100개씩 넘긴다.
             *
             */
            em.flush();
            em.clear();
            String modifiedFetchQuery = "select t from Team t";
            List<Team> result1 = em.createQuery(modifiedFetchQuery, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for(Team t : result1){
                System.out.println("team : " + t.getName() + ", size : " + t.getMembers().size());
                for(Member m : t.getMembers()){
                    System.out.println("\tMember = " + m);
                }
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
