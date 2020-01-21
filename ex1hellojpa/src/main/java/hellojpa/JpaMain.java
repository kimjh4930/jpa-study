package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  즉시로딩 지연로딩
     *
     *  - Member를 조회 할 때, Team 도 같이 조회해야 할까?
     *
     *  - Team에 fetch = LAZY 로 설정하면, Team 내부 값을 실제 사용 할 때, Team 을 초기화 한다.
     *      - Member만 조회 할 때는 Team을 Proxy로 가져옴.
     *      - Member를 부르고 Team을 부를 때 Query가 두번 발생함.
     *
     *  - Member와 Team을 거의 항상 같이 조회한다면?
     *      - LAZY 로딩 할 필요가 없음. 이떈 EAGER을 사용.
     *
     *  - 프록시와 즉시로딩 주의
     *      - 가급적 지연로딩만 사용 (특히 실무에서)
     *      - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생함.
     *      - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
     *          - JPQL은 Query문이 SQL문으로 번역됨.
     *          - Member에서 Team이 즉시로딩으로 되어있으면, Team을 가져오기위해 별도의 Query가 또 만들어짐.
     *          - Member 안에 연관관계가 N개 있으면, Query가 N개 만들어짐.
     *          - N(연관관계 쿼리) + 1(요청한 쿼리) 문제가 발생함.
     *          - LAZY로 설정하고 Member 요청시 Team도 같이 가져오고 싶으면 fetch join을 사용한다.
     *      - @ManyToOne, @OneToOne 은 기본 설정이 즉시로딩(EAGER)
     *      - @OneToMany, @ManyToMany 는 기본 설정이 지연로딩(LAZY)
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
            member.setName("user1");
            member.setTeam(team);

            em.persist(member);


            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());

            // Member 를 Proxy로 가져온다.
            // m : class hellojpa.Team$HibernateProxy
            System.out.println("m : " + m.getTeam().getClass());


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
