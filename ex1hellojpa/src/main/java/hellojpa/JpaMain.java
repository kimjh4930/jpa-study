package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

public class JpaMain {

    /**
     *  @MappedSuperclass
     *
     *  - 공통 매핑 정보가 필요할 떄 사용.
     *  - 상속관계 매핑이 아니다
     *  - 엔티티가 아니고, 테이블과 매핑되지 않는다.
     *  - 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
     *  - 부모 타입으로 조회되지 않는다.
     *      - em.find(BaseEntity.class, ID);
     *  - 직접 구현해서 사용 할 일이 없으므로, 추상클래스로 구현하길 권장함.
     *  - 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑정보를 모으는 역할
     *  - 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용.
     *  - 참고
     *      - @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속 가능하다.
     *
     */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Member member = new Member();
            member.setName("user1");
            member.setCreatedBy("Kim");
            member.setCreatedDate(LocalDateTime.now());

            em.persist(member);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
