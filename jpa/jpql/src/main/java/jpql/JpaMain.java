package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  벌크 연산
 *      - PK 하나를 대상으로 update, delete 하는 연산을 제외한
 *        update, delete를 벌크 연산이라고 생각하면 된다.
 *        ex) 재고가 10개 미만인 모든 상품의 가격으로 10% 상승하려면?
 *          -> JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL을 실행한다.
 *             : 변경된 게이터가 100건이라면 udpate sql이 100번 실행된다.
 *
 *  벌크 연산 예제
 *      - 쿼리 한 번으로 여러 테이블 row 변경
 *      - executeUpdate() 결과는 영향받은 엔티티의 개수가 나옴.
 *      - UPDATE, DELETE 지원
 *      - INSERT(insert into .. select, 하이버네이트 지)
 *
 *  주의점
 *      - 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리가 들어간다.
 *          - 벌크 연산을 먼저 실행. (이때 em.flush()가 실행된다.)
 *          - 벌크 연산 수행 후 영속성 컨텍스트를 초기화한다.
 *              : em.clear()
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
             *  아래 쿼리를 실행해도 영속성 컨텍스트에는 반영이 되어있지 않다.
             *  DB에는 반영되어있다.
             *  아래 명령을 수행하고 em.clear() 를 수행해야함.
             *  데이터 정합성이 맞지 않다.
             *
             *  update Member
             *  set
             *      age=20
             *
             *  member age : 0
             */
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("member age : " + member.getAge());
            System.out.println("resultCount : " + resultCount);

        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();

    }
}
