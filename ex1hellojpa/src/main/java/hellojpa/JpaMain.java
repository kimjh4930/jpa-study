package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  단방향 연관관계
     *      - 객체와 테이블 연관관계의 차이를 이해
     *      - 객체의 참조와 테이블의 외래 키를 매핑
     *      - 용어 이해
     *        - 방향(Direction) : 단방향, 양방향
     *        - 다중성(Multiplicity) : 다대일(N:1), 일대다(1:N), 일대일(1:1), 다대다 (N:M)
     *        - 연관관계 주인(Owner) : 객체 양방향 연관관계는 관리 주인이 필요.
     */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            System.out.println("findTeam : " + findTeam.getName());

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
