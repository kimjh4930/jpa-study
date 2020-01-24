package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

public class JpaMain {

    /**
     *  임베디드 타입
     *      - 새로운 값 타입을 직접 정의할 수 있음.
     *      - JPA는 임베디드 타입이라 함
     *      - 주로 기본 값 타입을 만들어서 복합 값 타입이라고도 함
     *      - int, String과 같은 값 타입
     *
     *  임베디드 타입 사용법
     *      - @Embeddable : 값 타입을 정의하는 곳에 표시
     *      - @Embedded : 값 타입을 사용하는 곳에 표시
     *      - 기본 생성자 필수
     *
     *  임베디드 타입의 장점
     *      - 재사용
     *      - 높은 응집도
     *      - Period.isWork() 처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음. -> 객체지향적인 설계 가능.
     *      - 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함.
     *
     *  임베디드 타입과 테이블 매핑
     *      - 데이터베이스의 Table 입장에서는 바뀔 것이 없다.
     *
     *  임베디드 타입과 테이블 매핑
     *      - 임베디드 타입은 Entity의 값일 뿐이다.
     *      - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다. (중요)
     *      - 객체와 테이블을 아주 세밀하게(fine-grained)매핑하는 것이 가능하다 (중요)
     *          - 회원 엔티티는 이름, 근무시작일, 근무 종료일, 주소 도시, 주소 번지, 주소 우편번호를 가진
     *          - 회원 엔티티는 이름, 근무기간, 집 주소를 가진다. -> 모델링을 설명하기 쉽고 깔끔하다.
     *      - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많다.
     *
     *  임베디드 타입과 연관관계
     *      - EmbeddedType이 Entity를 가질 수 있다.
     *      - EmbeddedType이 Entity의 FK 만 가지고 있으면 됨.
     *
     *  속성 재정의 : @AttributeOverride
     *      - 한 엔티티에서 같은 값 타입을 사용한다면?
     *
     *  임베디드 타입의 값이 null이면 매핑한 컬럼 값은 모두 Null.
    */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Member member = new Member();
            member.setName("hello");
            member.setHomeAddress(new Address("city", "street", "zipcode"));
            member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));

            em.persist(member);

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
