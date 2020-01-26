package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  페이징 API
 *      - JPA는 페이징을 다음 두 API로 추상화
 *          - setFirstResult(int startPosition) : 조회 시작 위치
 *          - setMaxResults(int maxResult) : 조회할 데이터
 *
 */

public class JpaMain {

    public static void main(String args[]){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            for(int i=0; i<100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            /*
                select
                    member0_.MEMBER_ID as MEMBER_I1_0_,
                    member0_.age as age2_0_,
                    member0_.TEAM_ID as TEAM_ID4_0_,
                    member0_.username as username3_0_
                from
                    Member member0_
                order by
                    member0_.age desc limit ? offset ?
             */
            List<Member> members = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)  // 몇 번째 부터
                    .setMaxResults(20)  // 몇개 가져올건지
                    .getResultList();

            System.out.println("size : " + members.size());

            for(Member m : members){
                System.out.println(m.toString());
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
