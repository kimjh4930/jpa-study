package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  값 타입과 불변객체
     *      - 값 타입은 복잡한 객체 새상을 조금이라도 단순화하려고 만든 개념.
     *      - 값 타입을 단순하고 안전하게 다룰 수 있어야 한다.
     *
     *  값 타입 공유 참조.
     *      - 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험하다.
     *          - 객체참조를 하면, 한쪽에서 값을 바꿨을 때, 같은 값을 참조하는 곳에서도 값이 변경된다.
     *
     *  값 타입 복사
     *      - 값 타입의 실제 인스턴스 값을 공유하는 것은 위험하다.
     *      - 대신 값을 복사해서 사용해야 한다.
     *      - 그러나 누군가가 실수로 복사해서 사용하지 않을 경우, compile level에서 side effect를 찾을 수 없다.
     *
     *  객체 타입의 한계
     *      - 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
     *      - 문제는 직접 정의한 값 타입은 객체 타입이라는 것
     *      - 기본타입은 값을 복사하지만, 객체는 참조한다.
     *      - 누군가가 객체를 복사하지 않고 사용하는 것을 막을 수 없다.
     *
     *  불변객체
     *      - 객체 타입을 수정 할 수 없게 만들면 부작용을 원천 차단 할 수 있다.
     *      - 값 타입은 불변객체로 설계해야 함.
     *      - 불변객체 : 생성 시점 이후 절대 값을 변경할 수 없는 객체로 만들어야 함.
     *      - 생성자로만 값을 설정하고 수정자를 만들지 않는다.
     *          - 혹은 내부에서만 사용 할 수 있게 수정자의 접근자를 private으로 선언한다.
     *      - 값을 변경하고 싶을 땐, 객체를 새로 생성한다.
     *
     *  불변이라는 제약으로 큰 재앙을 막을 수 있다.
    */

   public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Address address = new Address("city", "street", "zipcode");

            Member member1 = new Member();
            member1.setName("hello");
            member1.setHomeAddress(address);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("hello2");

            //값을 복사해서 사용해야 한다.
            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
            //member2.setHomeAddress(address);
            member2.setHomeAddress(copyAddress);

            em.persist(member2);

            /**
             *  같은 임베디드 값타입 객체를 참조하면, member1 의 주소를 변경할 때 member2의 주소도 같이 변경된다.
             *  Update Query가 두번 발생한다.
             *
             *  member1에 대한 query
             *  update Member set city=?, street=?, zipcode=?, USERNAME=?, endDate=?, startDate=? where MEMBER_ID=?
             *
             *  member2에 대한 query
             *  update Member set city=?, street=?, zipcode=?, USERNAME=?, endDate=?, startDate=? where MEMBER_ID=?
             *
             */
            //불변객체로 만들면 아래 작업을 원천봉쇄 할 수 있다.
            //member1.getHomeAddress().setCity("newCity");

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
