package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  고급 매핑 (상속 관계 매핑)
     *
     *  상속관계 매핑
     *      - 관계형 데이터베이스는 상속 관계가 없음.
     *      - 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
     *      - 상속관계 매핑 : 객체의 상속과 구조와 DB의 슈퍼타입 서브타입관계를 매핑
     *          - 조인전략 : 각각 테이블로 변환 (정)
     *              - 장점 : 정규화 되어있다. 제약조건을 상위 클래스에서 정할 수 있다. 저장공간 효율화
     *              - 단점 : 조회시 조인을 많이 사용함, 조회 쿼리가 복잡함, 데이터 저장시 insert sql을 두번 호출.
     *          - 단일 테이블 전략 : 통합 테이블로 변환
     *              - 장점 : 조인이 필요 없음. 조회 쿼리가 단순함
     *              - 단점 : 자식 엔티티가 매핑한 컬럼은 모두 Null을 허용해야 함 -> 치명적인 단점.
     *                      단일 테이블에 모든것을 저장하므로 테이블이 커질 수 있다.
     *                      상황에 따라서 성능이 더 안좋아 질 수 있다.
     *              - Query가 하나만 나감.
     *          - 구현 클래스마다 테이블 전략 : 서브타입 테이블로 전
     *              - 사용하면 안되는 전략.
     *              - 추상클래스로 조회하는 경우 union 으로 하위 클래스를 모두 조회한다.
     *                  select
     *                      item0_.id as id1_2_0_,
     *                      item0_.name as name2_2_0_,
     *                      item0_.price as price3_2_0_,
     *                      item0_.artist as artist1_0_0_,
     *                      item0_.author as author1_1_0_,
     *                      item0_.isbn as isbn2_1_0_,
     *                      item0_.actor as actor1_6_0_,
     *                      item0_.director as director2_6_0_,
     *                      item0_.clazz_ as clazz_0_
     *                  from
     *                      ( select
     *                          id,
     *                          name,
     *                          price,
     *                          artist,
     *                          null as author,
     *                          null as isbn,
     *                          null as actor,
     *                          null as director,
     *                          1 as clazz_
     *                  from
     *                      Album
     *                          ...
     *              - 장점 : 서브 타입을 명확하게 구분해서 처리할 때 효과적. not null 제약조건 사용 가능.
     *              - 단점 : 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL)
     *                      자식 테이블을 통합해서 관리하기 어려움
     *                          ex) 새로운 타입이 추가 될 때. 고쳐야 할 코드가 많아짐.
     *
     *
     *
     *  주요 매핑 전략
     *      - @Inheritance(strategy = InheritanceType.XXX)
     *          - JOINED : 조인전략
     *          - SINGLE_TABLE : 단일 테이블 전략
     *          - TABLE_PER_CLASS : 구현 클래스마다 테이블 전략
     *      - @DiscriminatorColumn(name="DTYPE")
     *      - @DiscriminatorValue("XXX")
     */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Movie movie = new Movie();
            movie.setDirector("aaaaa");
            movie.setActor("bbbb");
            movie.setName("바람과 함께 사라지다.");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();

            /**
             *  select
             *         movie0_.id as id1_2_0_,
             *         movie0_1_.name as name2_2_0_,
             *         movie0_1_.price as price3_2_0_,
             *         movie0_.actor as actor1_6_0_,
             *         movie0_.director as director2_6_0_
             *     from
             *         Movie movie0_
             *     inner join
             *         Item movie0_1_
             *             on movie0_.id=movie0_1_.id
             *     where
             *         movie0_.id=?
             *
             *  Item 에서 innerjoin 으로 값들을 가져온다.
             */

//            em.find(Movie.class, movie.getId());

            Item item = em.find(Item.class, movie.getId());
            System.out.println("item : " + item);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
