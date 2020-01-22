package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  영속성 전이와 고아객체
     *
     *  영속성 전이 : CASCADE
     *      - 특정 엔티티를 영속 상태로 만들 떄 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때 사용.
     *      - parent를 영속성 컨텍스트에 올릴 때, Parent에 연관된 Child도 같이 올리겠다는 의미.
     *          - em.persist(parent) 만 설정하면 child 도 같이 영속성 컨텍스트에 올라감.
     *      - 영속성 전이는 연관관계를 매핑하는 것과 아무런 관련이 없음.
     *      - 엔티티를 영속화 할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공 할 뿐.
     *      - Option
     *          - ALL       : Life Cycle을 맞출 때 사용
     *          - PERSIST   : 저장 Life Cycle을 맞출 때 사용.
     *          - REMOVE
     *
     *      - 언제 사용해야하나?
     *          - 한 부모가 자식들을 관리 할 때 사용.
     *          - 자식을 여러 부모가 관리하는 경우에는 사용하면 안됨. -> 여러 부모가 관리 할 땐 따로따로 관리해야 한다.
     *          - 소유자가 하나일 때만 사용
     *
     *  고아 객체
     *      - 고아 객체 제거 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
     *      - orphanRemoval = true
     *      - 참조하는 곳이 하나일 떄 사용해야 함!
     *      - 특정 엔티티가 개인 소유할 때 사용
     *      - 부모가 삭제되면 부모의 자식들은 전부 고아가 되므로 CascadeType.REMOVE 처럼 동작한다.
     *
     *  영속성 전이 + 고아 객체, 생명주기
     *      - 두 옵션을 모두 활성화하면 부모의 생명주기로 자식의 생명주기를 제어 할 수 있음.
     *      - DDD에서 AggregateRoot 개념을 구현 할 때 유용.
    */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            /**
             *  em.persist를 세 번 호출해야 함.
             *
             *  parent를 입력 할 때, child도 알아서 들어갔으면 좋겠다
             *      : CascadeType.ALL 로 설정한다.
             */
            em.persist(parent);
            em.persist(child1);
            em.persist(child2);

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
