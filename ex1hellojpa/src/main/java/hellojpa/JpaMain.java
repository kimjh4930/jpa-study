package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  일대다 매핑
     *      - 1 쪽이 연관관계의 주인.
     *      - N 쪽에 외래키를 가짐
     *      - 객체와 테이블의 차이 때문에 반대편 테이블에서 외래 키를 관리하는 독특한 구조를 갖게 됨.
     *      - @JoinColumn을 사용해야함. 그렇지 않으면 조인 테이블 방식을 사용함.
     *          - create table Team_Member (
     *            Team_TEAM_ID bigint not null,
     *            members_id bigint not null
     *            )
     *            @JoinColumn을 사용하지 않으면, 위와 같이 간 테이블이 추가 된다.
     *
     *      - 단점
     *          - 실제업무에서 수많은 테이블이 엮여서 돌아간다.
     *            insert query가 발생하면서 update가 발생하면 혼돈이 발생 할 수 있다.
     *            외래키를 들고있는 쪽에서 연관관계를 설정하는 것이 적절함.
     *          - 엔티티가 관리하는 외래 키가 다른 테이블에 있음.
     *          - 연관관계 관리르 루이해 추가로 UPDATE SQL을 실행함.
     *
     */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Member member = new Member();
            member.setName("memberA");
            em.persist(member);

            Team team = new Team();
            team.setName("teamA");
            team.getMembers().add(member);
            em.persist(team);

            /**
             *  insert into Team (name, TEAM_ID) values (?, ?)  //insert query가 발생하고
             *  update Member set TEAM_ID=? where id=?          //member 정보를 update 한다.
             */

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
