package jpql;

import javax.persistence.*;

/**
 *  JPQL 기본 문법과 기능
 *      - JPQL은 객체지향 쿼리 언어.
 *          - 테이블을 대상으로 쿼리를 만드는 것이 아니라, 엔티티를 대상으로 쿼리를 만든다.
 *      - JPQL은 SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.
 *      - JPQL은 SQL로 변환된다.
 *
 *  JPQL 문법
 *      - select m from Member as m where m.age > 18
 *          - Member, m.age -> 대소문자 구분.
 *      - JPQL 키워드는 대소문자 구분하지 않음 (SELECT, FROM, WHERE)
 *      - 엔티티 이름 사용, 테이블 이름이 아님
 *      - 별칭은 필수(m) as 는 생략 가능.
 *
 *  집합과 정렬
 *      - select COUNT(m)   // 회원수
 *               SUM(m.age) // 나이 합
 *               AVG(m.age) // 평균 나이
 *               MAX(m.age) // 최대 나이
 *               MIN(m.age) // 최소 나이
 *        from Member m
 *      - GROUP BY, HAVING
 *      - ORDER BY
 *
 *  TypedQuery, Query
 *      - TypedQuery : 반환타입이 명확할 때 사용
 *      - Query : 반환 타입이 명확하지 않을 떄 사용
 *
 *  결과 조회 API
 *      - query.getResultList() -> 결과가 Collection일 경우에 사용.
 *          - 결과가 없을땐 빈 리스트를 반환
 *          - NullPointException 를 막을 수 있다.
 *      - query.getSingleResult() -> 결과가 하나일 때 사용.
 *          - 결과가 정확히 하나일 때 사용해야 함.
 *          - NoResultException -> 결과가 없을 때
 *          - NonUniqueResultException -> 결과가 두개 이상 일 때
 *
 *  파라미터 바인딩
 *      - 이름기준
 *          - select m from Member m where m.username =: username
 *          - query.setParameter("username", usernameParam)
 *      - 위치기준 -> 사용하지 말 것.
 *
 */

public class JpaMain {

    public static void main(String args[]){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 타입 정보가 명확할 때 TypedQuery 를 사용함.
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);

            // 반환값이 String으로 며확하므로 사용 가능.
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);

            // 반환값이 명확하지 않을 땐 Query를 사용해야 함.
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            //파라미터 바인딩
            TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username","member1");

            Member singleResult = query4.getSingleResult();
            System.out.println("single : " + singleResult.getUsername());



        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();

    }
}
