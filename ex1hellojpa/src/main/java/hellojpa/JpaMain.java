package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
<<<<<<< HEAD
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
=======
     *  Proxy
     *
     *  em.find() vs em.getReference()
     *      - em.find() : 데이터베이스를 통해서 실제 엔티티 객체를 조회
     *      - em.getReference() : 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회.
     *
     *  em.find()
     *          select
     *         member0_.MEMBER_ID as MEMBER_I1_3_0_,
     *         member0_.createdBy as createdB2_3_0_,
     *         member0_.createdDate as createdD3_3_0_,
     *         member0_.lastModifiedBy as lastModi4_3_0_,
     *         member0_.lastModifiedDate as lastModi5_3_0_,
     *         member0_.USERNAME as USERNAME6_3_0_,
     *         member0_.TEAM_ID as TEAM_ID7_3_0_,
     *         team1_.TEAM_ID as TEAM_ID1_7_1_,
     *         team1_.createdBy as createdB2_7_1_,
     *         team1_.createdDate as createdD3_7_1_,
     *         team1_.lastModifiedBy as lastModi4_7_1_,
     *         team1_.lastModifiedDate as lastModi5_7_1_,
     *         team1_.name as name6_7_1_
     *     from
     *         Member member0_
     *     left outer join
     *         Team team1_
     *             on member0_.TEAM_ID=team1_.TEAM_ID
     *     where
     *         member0_.MEMBER_ID=?
     *
     *  em.getReference()
     *      - 위 메소드를 호출 했을 땐 아무런 쿼리가 발생하지 않음.
     *      - ID 를 조회 할 땐 member.getId() 로 이미 정보를 넣어서 쿼리문이 발생하지 않음.
     *      - getName() 을 했을 때, Query문이 나간다.
     *      - 조회한 클래스를 출력하면 아래와 깉이 나옴. 아래 클래스는 JPA가 임의로 생성한 Proxy 객체.
     *          - findMember : class hellojpa.Member$HibernateProxy
     *
     *  프록시 특징
     *      - 실제 클래스를 상속 받아서 만들어 짐.
     *      - 실제 클래스와 겉 모양이 같다.
     *      - 이론상 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용해도 됨.
     *      - 프록시 객체는 실제 객체의 참조를 보관
     *      - 프록시 객체를 호출하면 프록시 객체는 실제 객제의 메소드를 호출.
     *      - 프록치 객체는 처음 사용할 때 한 번만 초기화.
     *      - 프록시 객체를 조기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능.
     *      - 프록시 객체는 원본 엔티티를 상속받음, 타입 체크시 주의해야 함 (== 비교대신 instanceof 를 사용해야 함.)
     *          - instanceof()로 하지 않으면, 실제 객체와 proxy 객체를 비교하게 됨.
     *      - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티를 반환.
     *      - 영속성 컨텍스트의 도움을 받을 수 없는 준영속상태일때, 프록시 초기화하면 문제가 발생한다.
     *
     *  프록시 확인
     *      - 프록시 인스턴스의 초기화 여부 확인
     *          - emf.getPersistenceUnitUtil().isLoaded(Object Entity)
     *          - PersistenceUnitUtil.isLoaded(Object Entity)
     *      - 프록시 클래스 확인 방법
     *          - entity.getClass().getName() 출력
     *      - 프록시 강제 초기화
     *          - org.hibernate.Hibernate.initialize(entity);
     *      - JPA표준에는 강제 초기화하는 방법이 없음.
>>>>>>> 4c015221868f8d1b5cd869d6685f368928f72f49
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
<<<<<<< HEAD
            member.setTeam(team);

            em.persist(member);

=======

            em.persist(member);

            em.flush();
            em.clear();

            /**
             *  m1 과 reference 가 동일한 이유
             *  1. 이미 영속성 컨텍스트에 올라와있어서 굳이 getReference() 에서 프록시를 반환 할 이유가 없음.
             *  2. JPA에서는 같은 transaction에서 a == a 를 보장해야 한다.
             */

            Member m1 = em.find(Member.class, member.getId());
            System.out.println("m1 : " + m1.getClass());

            Member reference = em.getReference(Member.class, member.getId());
            System.out.println("reference : " + reference.getClass());

            System.out.println("==? : " + (m1 == reference));

            Member member2 = new Member();
            member.setName("user2");

            em.persist(member2);
>>>>>>> 4c015221868f8d1b5cd869d6685f368928f72f49

            em.flush();
            em.clear();

<<<<<<< HEAD
            Member m = em.find(Member.class, member.getId());

            // Member 를 Proxy로 가져온다.
            // m : class hellojpa.Team$HibernateProxy
            System.out.println("m : " + m.getTeam().getClass());

=======
            /**
             *  getReference()를 수행해서 영속성 컨텍스트에 proxy를 올려놓고,
             *  같은 요소를 em.find()로 조회해도 똑같이 Proxy가 반환된다.
             *
             *  "transaction 안에서는 동일함을 보장한다."
             *
             *  Proxy이든 아니든 관계없지 개발하는 것이 중요.
             */

            Member refMember = em.getReference(Member.class, member2.getId());
            System.out.println("refMember : " + refMember.getClass());  //proxy

            Member findMember = em.find(Member.class, member2.getId());
            System.out.println("findMember : " + findMember.getClass());    //proxy

            System.out.println("refMember == findMember : " + (refMember == findMember));

            /**
             *  준영속상태일 때 proxy를 초기화 하는 경우
             *      - LazyInitializationException 이 발생함.
             */

            Member member3 = new Member();
            member3.setName("user3");

            Member ref3 = em.getReference(Member.class, member3.getId());
            System.out.println("ref3 : " + ref3.getClass());

            em.detach(member3);
            //em.clear(), em.close() 등 영속성 컨텍스트를 종료하면 에러 발생.

            ref3.getName(); // 영속성 컨텍스트이 도움을 받지 못해서, exception 발생.
>>>>>>> 4c015221868f8d1b5cd869d6685f368928f72f49

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
