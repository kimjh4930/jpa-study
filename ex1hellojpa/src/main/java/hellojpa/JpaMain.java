package hellojpa;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

public class JpaMain {

    /**
     *  값 타입 컬렉션
     *      - 값 타입을 하나 이상 저장할 때 사용.
     *      - @ElementCollection, @CollectionTable 사용해서 Mapping 한다.
     *      - 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
     *      - 컬렉션을 저장하기 위한 별도의 테이블이 필요하다.
     *
     *  값 타입 컬렉션 사용
     *      - 값 타입 컬렉션은 라이프 사이클을 갖지 않는다. 라이프 사이클은 Member에 의존한다.
     *      - 값 타입 컬렉션은 영속성 전이 + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다.
     *
     *  값 타입 컬렉션의 제약사항
     *      - 값 타입은 엔티티와 다르게 식별자 개념이 없다.
     *      - 값은 변경하면 추적하기 어렵다.
     *      - (중요)값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고,
     *        값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
     *      - 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야 한다. (null 입력 x, 중복 저장 x)
     *      -> 사용하면 안된다. 다른 방법으로 해결해야 한다.
     *
     *  값 타입 컬렉션 대안
     *      - 실무에서는 상황에 따라 값 타입 컬렉션 대신 일대다 관계를 고려해야 한다.
     *      - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용한다.
     *           - 값 타입을 엔티티로 승급한다. (Address -> AddressEntity)
     *      - 영속성 전이 + 고아 객체 제를 사용해서 값 타입 컬렉션 처럼 사용.
     *      - 정말 단순 할 때 값 타입 컬렉션을 사용
     *
     *  정리
     *      - 값 타입은 정말 값 타입이라 판단될 때만 사용
     *      - 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안된다.
     *      - 식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 엔티티로 만든다.
     */

   public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            //값 타입 저장 예
            Member member = new Member();
            member.setName("member1");
            member.setHomeAddress(new Address("city1", "street", "zipcode"));
            member.getFavorateFood().add("치킨");
            member.getFavorateFood().add("족발");
            member.getFavorateFood().add("피자");

//            member.getAddressHistory().add(new Address("old1", "street", "zipcode"));
//            member.getAddressHistory().add(new Address("old2", "street", "zipcode"));

            member.getAddressHistory().add(new AddressEntity("old1", "street", "zipcode"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "zipcode"));


            em.persist(member);

            em.flush();
            em.clear();

            /**
             *  값 타입 조회 예제
             *  select
             *         member0_.MEMBER_ID as MEMBER_I1_6_0_,
             *         member0_.city as city2_6_0_,
             *         member0_.street as street3_6_0_,
             *         member0_.zipcode as zipcode4_6_0_,
             *         member0_.USERNAME as USERNAME5_6_0_
             *     from
             *         Member member0_
             *     where
             *         member0_.MEMBER_ID=?
             *
             *      - Collection 들은 지연로딩 된다.
             *          - 기본값이 LAZY로 설정되어있음.
             */
            System.out.println("================ START =================");
            Member findMember = em.find(Member.class, member.getId());

//            List<Address> addressHistory = findMember.getAddressHistory();
//
//            for(Address address : addressHistory){
//                System.out.println("address : " + address.getCity());
//            }

            Set<String> favoriteFoods = findMember.getFavorateFood();

            for(String favoriteFood : favoriteFoods){
                System.out.println("favoriateFood : " + favoriteFood);
            }

            /**
             *  값 타입 수정 예제
             *      - Setter 를 이용해서 수정하면 절대 안된다.
             *      - 새로 불변객체를 만들어 갈아 끼워야 한다.
             *      - List 나 Set 을 변경 할 땐, Delete 후 Insert를 수행한다.
             */

//            findMember.getHomeAddress().setCity("newCity");
            Address oldAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", oldAddress.getStreet(), oldAddress.getZipcode()));

            //치킨을 한식으로 변경한다.
            findMember.getFavorateFood().remove("치킨");
            findMember.getFavorateFood().add("한식");

            /**
             *  주소 변경 old1 -> newCity1
             *  equals hashcode를 잘못 넣으면 지워지지 않는다.
             *
             *  delete from ADDRESS where MEMBER_ID=?
             *  insert into ADDRESS (MEMBER_ID, city, street, zipcode) values (?, ?, ?, ?)
             *  insert into ADDRESS (MEMBER_ID, city, street, zipcode) values (?, ?, ?, ?)
             *      -> insert query가 두번 발생한다.
             *      1. MEMBER_ID 와 관련된 내용을 다 지운다.
             *      2. 새로 Table에 저장한다.
             *
             */
            System.out.println("========== update address history ==========");
//            findMember.getAddressHistory().remove(new Address("old1", "street", "zipcode"));
//            findMember.getAddressHistory().add(new Address("newCity1", "street", "zipcode"));

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
