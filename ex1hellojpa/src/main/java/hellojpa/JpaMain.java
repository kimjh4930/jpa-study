package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  대다대 연관관계.
     *      - 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현 할 수 없음.
     *      - 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야 함.
     *      - 객체는 걸렉션을 사용해서 객체 2개로 다대다 관계 가능.
     *
     *  다대다 매핑의 한계
     *      - 편리해 보이지만 실무에서 사용 할 수 없음.
     *      - 연결 테이블이 단순히 연결만 하고 끝나지 않음.
     *      - 주문시간, 수량 같은 데이터가 들어올 수 있음.
     *
     *  다대다 한계 극복
     *      - 연결 테이블용 엔티티 추가 (연결 테이블을 엔티티로 승격) : MemberProduct.java
     *      - @ManyToMany -> @OneToMany, @ManyToOne
    */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
