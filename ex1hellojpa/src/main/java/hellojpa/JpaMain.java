package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  일대일 관계
     *      - 주 테이블이나 대상 테이블 중 외래 키 선택가능
     *      - 외래키에 데이터베이스 유니크(UNI) 제약조건 추가
     *      - 다대일 단방향 연관관계와 비슷.
     *
     *      - 대상 테이블에 외래 키 단방향 : 지원하지 않음. (LOCKER에서 MEMBER 를 FK 로 설정하는 경우를 의미)
     *          - 양방향 관계에서는 대상 테이블에서 외래키를 가질 수 있음. -> 대상테이블과 주 테이블을 분리해서 생각 할 필요가 없음.
     *  주 테이블에 외래키
     *      - 주 객체가 대상 객체의 참조를 가지는 것 처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾음
     *      - 객체지향 개발자 선호
     *      - JPA 매핑편리
     *      - 장점 : 주 테이블만 조회해도 대상 테이블에 데이터 있는지 확인가능. (Member 로만 조회해도 Member와 연결된 Locker가 있는지 확인 가능.)
     *      - 단점 : 값이 없으면 외래 키에 null을 허용해야 함.
     *  대상 테이블에 외래 키
     *      - 대상 테이블에 외래 키가 존재
     *      - 전통적인 데이터베이스 개발자가 선호함.
     *      - 장점 : 주 테이블과 대상 테이블을 일대일 관계에서 일대다 관계로 변경할 때 테이블 구조 유지
     *      - 단점 : 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨.
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
