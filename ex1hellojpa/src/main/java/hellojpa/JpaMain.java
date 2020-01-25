package hellojpa;

import javax.persistence.*;

public class JpaMain {

    /**
     *  값 타입의 비교
     *      - 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 봐야 한다.
     *      - 동일성(identity)비교 와 동등성(equivalence)비교
     *          - 동일성 비교 : 인스턴스의 참조 값을 비교, == 사용
     *          - 동등성 비교 : 인스턴스의 값을 비교, equals() 사용
     *      - 값타입은 a.equals(b)를 사용해서 동등성 비교를 해야 함.
     *      - 값 타입은 equals() 메소드를 적절하게 재정의(주로 모든 필드를 비교) 한다.
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
