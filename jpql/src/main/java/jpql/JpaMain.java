package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  JPQL 타입 표현
 *      - 문자 : 'HELLO', 'SHE''s'' //single quatation 을 넣으려면 두개를 넣는다.
 *      - 문자 : 10L(Long), 10D(Double), 10F(Float)
 *      - Boolean : TRUE, FALSE
 *      - ENUM : jpabook.MemberType.Admin -> 패키지명을 포함해서 넣는다.
 *      - Entity Type : Type(m) = Member (상속 관계에서 사용)
 *          - select i from Item i where type(i) == Book
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
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m.username, 'HELLO', TRUE from Member m " +
                    "where m.type = :userType";
            List<Object[]> resultList = em.createQuery(query)
                    .setParameter("userType", jpql.MemberType.ADMIN)
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
