package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  프로젝션(SELECT)
 *      - SELECT절에 조회할 대상을 지정하는 것
 *      - 프로젝션 대상 : Entity, Embedded 타입, 스칼라 타입(숫자, 문자 등 기본데이터 타입)
 *      - DISTINCT 로 중복 제거
 *
 *  여러 값 조회
 *      - Query 타입으로 조회
 *      - Object[] 타입으로 조회
 *      - new 명령어로 조회
 *          - 단순 값을 DTO로 바로 조회
 *              - SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM member m
 *          - 패키지 명을 포함한 전체 클래스 명 입력
 *          - 순서와 타입이 일치하는 생성자 필요
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

            em.flush();
            em.clear();


            List<Team> result = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();
            /**
             *  select
             *      team1_.TEAM_ID as TEAM_ID1_3_,
             *      team1_.name as name2_3_
             *  from
             *      Member member0_
             *         inner join
             *             Team team1_
             *                 on member0_.TEAM_ID=team1_.TEAM_ID
             *
             *   => SQL문과 비슷하게 맞춰줘야 한다.
             */

            // 동일한 쿼리가 나가지만, 아래와 같이 작성하는 것이 바람직하다.
            // Query를 튜닝해야 하는 입장에서는 아래처럼 적는것이 더 명확하다.
            List<Team> result1 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            //스칼라 타입
            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

            Object[] o = resultList.get(0);

            System.out.println("username : " + o[0]);
            System.out.println("age : " + o[1]);

            //DTO로 조회
            List<UserDTO> userDTOs = em.createQuery("select new jpql.UserDTO(m.username, m.age) from Member m")
                    .getResultList();

            for(UserDTO dto : userDTOs){
                System.out.println("dto username : " + dto.getUsername());
                System.out.println("dto age : " + dto.getAge());
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
