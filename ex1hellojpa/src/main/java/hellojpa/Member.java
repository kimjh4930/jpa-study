package hellojpa;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50)
//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {

    /**
     *  직접할당 : @Id 만 사용
     *  자동생성 : @GeneratedValue
     *      - IDENTITY
     *          : 데이터베이스에 위임 (주로 MySQL, SQL Server, DB2 에서 사용)
     *            기본키 생성을 데이터베이스에 위임
     *            데이터베이스에 INSERT Query를 실행한 후에 ID 값을 확인 할 수 있음. -> 단점.
     *            단점을 보완하기 위해 em.persist() 를 호출하는 시점에 database에 insert query를 보낸다.
     *
     *      - SEQUENCE
     *          : 데이터베이스 시퀀스 오브젝트 사용, ORACLE
     *            @SequenceGenerator 필요
     *            initialValue, allocationSize
     *              - next call을 할 때, 미리 DB에 50개를 설정하고, memory에서 개수만큼 사용.
     *                동시성 이슈 없이 다양한 문제를 해결 할 수 있다.
     *
     *                Hibernate:
     *                  call next value for MEMBER_SEQ | 1 (initial value)
     *                Hibernate:
     *                  call next value for MEMBER_SEQ | 51
     *                member id : 1  | sequence 1, 51 을 가져옴.
     *                member id : 2  | memory 에서 가져옴.
     *                member id : 3  | memory 에서 가져옴.
     *
     *
     *      - TABLE
     *          : 키 생성용 테이블을 만들어서 데이터베이스 시퀀스를 흉내내는 전략.
     *            장점 : 모든 데이터베이스에 적용가능
     *            단점 : 성능, 실제 운영에서 사용하기에는 부담스러움. DB관례에 맞게 사용하는 것을 추천함.
     *            @TableGenerator 필요
     *
     *      - AUTO
     *          : 방언에 따라 자동 지정, 기본값
     *
     *  권장하는 식별자 전략
     *      - 기본 키 제약 조건 : null이 아님, 유일해야하고, 변하면 안된다. (변하기 어려운 조건을 찾기 어려움.)
     *        : 권장 : Long형 + 대체키(Random 값, Sequence 전략) + 키 생성전략 사용
     *        : Auto_increment, sequence, uuid
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "name")
    private String username;

    public Member (){ }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
