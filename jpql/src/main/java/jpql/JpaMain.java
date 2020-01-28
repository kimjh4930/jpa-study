package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 *  JPQL - 경로 표현식
 *      - .(점) 을 찍어 객체 그래프를 탐색하는 것.
 *          select m.username   -> 상태 필드
 *          from Member m
 *              join m.team t   -> 단일 값 연관 필드
 *              join m.orders o -> 컬렉션 값 연관 필드 (orders 가 Collection)
 *          where t.name = '팀A'
 *
 *  상태 필드 : 단순히 값을 저장하기 위한 필드 (m.username)
 *      - 경로 탐색의 끝, 탐색하지 않는다.
 *
 *  연관 필드 : 연관관계를 위한 필드
 *      - 단일 값 연관 필드
 *          - @ManyToOne, @OneToOne, 대상이 엔티티
 *          - 묵시적 내부 조인(inner join) 발생, 탐색을 한다.
 *      - 컬렉션 값 연관 필드
 *          - @OneToMany, @ManyToMany, 대상이 Collection
 *
 *  주의 사항
 *      - 항상 내부 조인
 *      - 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야 함
 *      - 경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM (JOIN)절에 영향을 준다.
 *
 *  실무 조언
 *      - 가급적 묵시적 조인 대신에 명시적 조인을 사용하자
 *          - 조인은 SQL튜닝에 중요 포인트
 *      - 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어렵다.
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

            /**
             * 경로탐색의 끝 더이상 탐색이 불가능 함.
             */
            String query = "select m.username from Member m";

            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            for(String s : result){
                System.out.println("result : " + s);
            }

            /**
             *  단일 값 연관 경로
             *  묵시적 내부 조인이 발생함, team 내부 탐색 가능.
             *
             *  select
             *      team1_.TEAM_ID as TEAM_ID1_3_,
             *      team1_.name as name2_3_
             *  from
             *      Member member0_
             *  inner join
             *      Team team1_
             *          on member0_.TEAM_ID=team1_.TEAM_ID
             *
             *  실무에서는 지양해야 함.
             *  join query가 발생하지만, 찾기 어렵다.
             *  Query 를 튜닝하기 어려움.
             */
            String query1 = "select m.team from Member m";
            List<Team> result1 = em.createQuery(query1, Team.class)
                    .getResultList();

            for(Team s : result1){
                System.out.println("result : " + s);
            }

            /**
             *  컬렉션 값 연관경로
             *      - 묵시적 내부 조인 발생, 내부 탐색이 불가능하다.
             *      - 사용하지 않는다.
             *      - FROM 절에서 명시적 조인을 통해 별칭을 얻으면, 그 별칭으로 탐색 할 수 있다.
             */
//            String query2 = "select t.members from Team t";
            String query2 = "select m.username From Team t join t.members m";
            List members = em.createQuery(query2, Collection.class)
                    .getResultList();

            for(Object m : members){
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
