package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    /**
     *  양방향 연관관계와 연관관계의 주인.
     *      - 객체 : 참조, 테이블 : 외래키로 연관관계를 맺음.
     */

    /**
     *  mappedBy : 객체와 테이블이 연관관계를 맺는 차이를 이해해야 함.
     *      - 객체 (양방향 연관관계 -> 사실은 단방향 연관관계가 2개 있는 것.)
     *          회원 -> 팀 (단방향)
     *          팀 -> 회원 (단방)
     *      - 테이블향
     *          회원 <-> 팀 (양방향)
     *          FK 만으로 양쪽의 내용을 모두 찾을 수 있다.향
     *          ---
     *          SELECT * FROM MEMBER M JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
     *          SELECT * FROM TEAM T JOIN MEMBER M ON T.TEAM_ID = M.TEAM_ID
     *
     *  MEMBER나 TEAM중 하나로 FK를 관리해야 함.
     *
     *  연관관계의 주인
     *      - 양방향 매핑 규칙
     *          - 객체의 두 관계 중 하나를 연관관계의 주인으로 설정해야 함.
     *          - 연관관계의 주인만이 외래 키를 관리 (등록, 수정)
     *          - 주인이 아닌쪽은 읽기만 가능
     *          - 주인은 mappedBy 속성을 사용 할 수 없음.
     *          - 주인이 아니면 mappedBy 속성으로 주인을 지정해야 함.
     *
     *  * 외래키가 있는 곳을 연관관계의 주인으로 설정한다.
     *  본 예제에서는 Member.team 이 연관관계의 주인.
     *
     *  N:1 에서 N쪽을 연관관계의 주인으로 설정.
     *  Member (N) : 연관관계의 주인리
     *
     *  연관관계 편의 메서드는 어디에 생성하든 상관없으나, 한 쪽에만 생성 할 것.
     *
     *  양방향 연관관계 주의
     *      - 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정
     *      - 연관관계 편의 메소드를 생성하자.
     *      = 양방향 매핑시에 무한 루프를 조심하자
     *          : toString(), lombok, JSON 생성 라이브러
     *          : Entity 를 DTO로 변환해서 JSON으로 내보낼 것.
     *          : Entity를 바로 내보내면, Entity가 변하면 API 스펙이 변함.
     *
     *  정리
     *      - 단방향 매핑만으로도 이미 연관관계 매핑은 완료됨.
     *      - 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추가된 것.
     *      - JPQL에서 역방향으로 탐색할 일이 많음.
     *      - 단방향 매핑을 잘 하고 양방향은 필요할 떄 추가해도 됨. (테이블에 영향을 주지 않는다.)
     *
     *  * 비즈니스 로직을 기준으로 연관관계의 주인을 선택하면 안됨.
     *      - 타이어와 자동차
     *          : 비즈니스 로직으로 생각하면 자동차가 타이어보다 중요하지만, 연관관계의 주인은 타이어로 설정한다.
    */

    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            team.addMember(member);

            em.persist(member);

            /**
             *  아래 작업을 하지 않으면 transaction이 끝나기 전에 입력한 Member값을 team.getMembers()로
             *  가져오려 할 때, 영속성 컨텍스트에는 반영되어있지 않아 Member를 가져 올 수 없다.
             *
             *  연관관계 편의 메서드를 이용해 양쪽에 값을 같이 넣어줘야 한다.
             *
             *  연관관계 편의 메서드를 적용 할 땐 Setter에 설정하지 않고, 메서드를 별도로 만든다.
             */

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            /**
             *  연관관계 편의 메서드를 이용해 양쪽에 값을 넣어주지 않으면, team.getMember()로 Member롤 찾을 수 없다.
             */
            for(Member m : members){
                System.out.println("m : " + m.getName());;
            }

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally{
            em.close();
        }

        emf.close();
    }
}
