package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  조인
 *      - 내부조인
 *          - SELECT m FROM Member m [INNER] JOIN m.team t
 *            : Member는 있고 Team 이 없으면 조회 안됨.
 *      - 외부조인
 *          - SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
 *            : Team 이 없어도 Member 전부 조회됨. team 은 null로 나옴.
 *      - 세타조인
 *          - SELECT count(m) from Member m, Team t where m.username = t.name
 *            : 연관관계가 전혀 없는 테이블 간 Join (막 조인이라 표현함)
 *
 *  ON 절
 *      - 조인 대상 필터링
 *          - 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
 *            JPQL : SELECT m, t, FROM Member m LEFT JOIN m.team t on t.name = 'A'
 *            SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.anem = 'A'
 *      - 연관관계가 없는 엔티티를 외부 조인 할 수 있다.
 *          - 회원의 이름과 팀의 이름이 같은 대상 외부 조인
 *            JPQL : SELECT m, t, FROM Member m LEFT JOIN Team t on m.username = t.name
 *            SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name
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

            Team team = new Team();
            team.setName("teamA");

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            //INNER JOIN (inner join)
            String innerJoinQuery = "select m from Member m join m.team t";
            List<Member> members = em.createQuery(innerJoinQuery, Member.class)
                    .getResultList();

            //OUTER JOIN (left outer join)
            String outerJoinQuery = "select m from Member m left join m.team t";
            List<Member> outerMembers = em.createQuery(outerJoinQuery, Member.class)
                    .getResultList();

            //THETA JOIN (cross)
            String thetaJoinQuery = "select m from Member m, Team t where m.username = t.name";
            List<Member> thetaMembers = em.createQuery(thetaJoinQuery, Member.class)
                    .getResultList();

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();

    }
}
