package jpql;

import javax.persistence.*;
import java.util.List;

/**
 *  조건식 - CASE 식식
 *      - 기본 CASE 식
 *      - 단순 CASE 식
 *      - Coalesce : 하나씩 조회해서 null이 아니면 반환
 *      - NULLIF : 두 값이 같으면 Null 반환, 다르면 첫 번째 값 반
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
            member.setUsername("관리자");
            member.setAge(10);
            member.changeTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            // 기본 Query
            String caseQuery = "select " +
                        "case when m.age <= 10 then '학생요금' " +
                            "when m.age >= 60 then '경로요금 '" +
                            "else '일반요금 '" +
                        "end " +
                    "from Member m";

            List<String> result = em.createQuery(caseQuery).getResultList();

            for(String r : result) {
                System.out.println("result : " + r);
            }

            // Coalesce
            String coalesceQuery = "select coalesce(m.username, '이름 없는 회원') " +
                    "from Member m";
            List<String> coalesceResult = em.createQuery(coalesceQuery, String.class).getResultList();

            for(String s : coalesceResult){
                System.out.println("coalesce Result : " + s);
            }

            // NULLIF
            // 관리자의 이름을 숨겨야 할 때 사용한다.
            String nullifQuery = "select nullif(m.username, '관리자') as username "+
                    "from Member m";
            List<String> nullifResult = em.createQuery(nullifQuery)
                    .getResultList();

            for(String s : nullifResult){
                System.out.println("nullif Result : " + s);
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
