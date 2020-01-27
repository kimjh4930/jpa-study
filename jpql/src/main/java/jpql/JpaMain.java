package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  서브쿼리
 *      - 나이가 평균보다 많은 회원
 *        : select m from Member m where m.age > (select avg(m2.age) from Member m2)
 *      - 한 건이라도 주문한 고객
 *        : select m from Member m where (select count(o) Order o where m = o.member) > 0
 *
 *  [NOT] EXIST : 서브쿼리에 결과가 존재하면 참
 *      - {ALL | ANY | SOME} (subquery)
 *      - ALL : 모두 만족이면 참.
 *      - ANY, SOME : 조건을 하나라도 만족하면 참
 *      - 예제
 *          - 팀 A 소속인 회원
 *            : select m from Member m where exist(select t from m.team t where t.name = '팀A')
 *          - 전체 상품 각각의 재고보다 주문량이 많은 주문들
 *            : select o from Orders o where o.orderAmount > ALL (select p.stockAmount from Product p)
 *          - 어떤 팀이든 팀에 소속된 회원
 *            : select m from Member m where m.team = ANY (select t from Team t)
 *
 *  [NOT] IN (subquery) : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
 *
 *  JPA 서브 쿼리 한계
 *      - JPA는 WHERE, HAVING 절에서만 서브쿼리를 사용 할 수 있다.
 *      - SELECT 절도 가능 (hibernate 에서 지원)
 *      - FROM 절의 서브 쿼리는 현재 JPQL에서 불가능.
 *          - 조인으로 풀 수 있으면 풀어서 해결한다.
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
