package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * JPA가 관리하는 객체임을 의미함.
 *  - name : 매핑할 테이블 이름.
 *  아래와 같이 Query가 생성됨.
 *    select
 *         member0_.id as id1_0_0_,
 *         member0_.name as name2_0_0_
 *     from
 *         MBR member0_ //Table 이름이 바뀐것을 확인 할 수 있음.
 *     where
 *         member0_.id=?
 *
 *  데이터베이스 스키아 자동생성
 *      - DDL을 애플리케이션 실행 시점에 자동으로 생성함.
 *      - 테이블중심 -> 객체중심
 *      - 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL을 생성함.
 *          - 이 DDL 은 개발장비에서만 사용 할 것.
 *          - 실무에서는 Table을 따로 생성하고 Mapping만 할 것.
 *          - 필요할 경우에는 적절히 다듬어서 사용 할 것.
 *
 *  주의
 *      - 기본 생성자 필수, (public, protected 를 허용)
 *      - final, enum, interface, enum 사용 불가
 *      - 저장할 필드에 final을 사용 할 수 없음.
 */
@Entity
public class Member {

    @Id
    private Long id;

    /**
     * JPA 실행에 영향을 주지 않음. DDL생성에만 영향을 준다.
     * JPA 실행 로직에는 영향을 주지 않음.
     */
    @Column(unique = true, length = 10)
    private String name;

    protected Member (){}

    public Member (Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
