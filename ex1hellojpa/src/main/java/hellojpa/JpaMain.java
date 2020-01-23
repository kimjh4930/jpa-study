package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  기본값 타입
     *
     *  JPA의 데이터 타입 분류
     *      - Entity 타입
     *          - @Entity로 정의하는 객체
     *          - 데이터가 변해도 식별자로 지속해서 추적 가능
     *          - 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능
     *      - 값 타입
     *          - 단순한 값으로 사용하는 자바 기본 타입이나 객체
     *          - 식별자가 없고 값만 있으므로 변경시 추적 불가
     *
     *
     *  값 타입 분류
     *      - 기본값 타입
     *          - 자바 기본 타입
     *          - 래퍼 클래스
     *          - String
     *      - 임베디드 타입 (복합 값 타입)
     *      - 컬렉션 값 타입 (Java Collection 에 기본값이나 임베디드 타입을 넣을 수 있는 값 타입)
     *
     *  값 타입 특징
     *      - 생명주기를 엔티티에 의존
     *          - 회원을 삭제하면 이름, 나이 필드도 함께 삭
     *      - 값 타입을 공유하면 안됨
     *          - 회원 이름 변경 시, 다른 회원의 이름도 함께 변경되면 안됨.
     *
     *  자바의 기본 타입은 절대 공유하지 못한다.
     *      - int, double 같은 기본 타입은 절대 공유하지 못한다.
     *      - 기본 타입 (Primitive type)은 항상 값을 복사한다.
     *      - Integer 같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만 변경하지 못한다.
     *
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
            e.printStackTrace();
        }finally{
            em.close();
        }

        emf.close();
    }
}
