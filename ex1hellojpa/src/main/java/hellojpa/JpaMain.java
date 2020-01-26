package hellojpa;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

public class JpaMain {

    /**
     *  JPQL 소개
     *      - JPA를 사용하면 Entity 객체를 중심으로 개발한다.
     *      - 검색쿼리가 문제
     *      - 검색할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색해야 한다.
     *      - 모든 DB 데이터를 객체로 변환해서 검ㅅ개하는 것은 불가능하다.
     *      - 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요하다.
     *      - JPQL 이라는 객체 지향 쿼리 언어를 제공.
     *          - JPQL : Entity 객체를 대상으로 쿼리를 만든다.
     *          - SQL : DB Table을 대상으로 쿼리를 만든다.
     *      - 특정 데이터베이스 SQL에 의존하지 않는다.
     *      - 객체지 SQL
     *
     *  Criteria -> 안쓴다.
     *      - JPQL의 문제점 : createQuery의 변수로 들어가는 내용은 단순 String.
     *          - 동적 Query를 만들기 어렵다.
     *          - Compile 단계에서 Error 를 검사하기 힘들다.
     *      - 장점
     *          - 동적 Query를 작성하기 편하다.
     *          - Compile 단계에서 Error 를 검사할 수 있다.
     *      - 단점
     *          - 유지보수가 어려움
     *          - 너무 복잡하고 실용성이 없다.
     *      - Criteria 대신에 QueryDSL 사용을 권장.
     *
     *  QueryDSL
     *      - 문자가 아닌 자바코드로 JPQL을 작성할 수 있음.
     *      - JPQL 빌더 역할
     *      - 컴파일 시점에 문법 오류를 찾을 수 있음.
     *      - 동적쿼리 작성이 편리하다.
     *      - 단순하고 쉬움
     *      - 실무에서 사용을 권장함.
     *
     *  JDBC 직접 사용, SpringJdbcTemplate 등
     *      - JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, Mybatis등을 함께 사용 가능.
     *      - 영속성 컨텍스트를 적절한 시점에 강제 플러시 필요.
     *      - JPA에서 제공하는 기능들은 Query를 날리기 전에 em.flush() 를 수행하지만
     *        JPA를 거치지 않고 DB에 접근하면 db에 저장하지 않은 상태에서 DB에 접근하게 된다.
     */

   public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            /**
             *  select
             *         m
             *     from
             *         Member m
             *     where
             *          m.name like '%kim%' select
             *          member0_.MEMBER_ID as MEMBER_I1_6_,
             *          member0_.city as city2_6_,
             *          member0_.street as street3_6_,
             *          member0_.zipcode as zipcode4_6_,
             *          member0_.USERNAME as USERNAME5_6_
             *   from
             *       Member member0_
             *   where
             *       member0_.USERNAME like '%kim%'
             */
            List<Member> findMembers = em.createQuery(
                    "select m from Member m where m.name like '%kim%'",
                    Member.class)
                    .getResultList();

            for(Member m : findMembers){
                System.out.println("member : " + m.getName());
            }

            //Criteria
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> cqMember = em.createQuery(cq).getResultList();

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();
    }
}
