package hellojpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *      create table Member (
 *        id bigint not null,
 *         age integer,
 *         createDate timestamp,
 *         description clob,
 *         lastModifiedDate timestamp,
 *         roleType varchar(255),
 *         name varchar(255),
 *         primary key (id)
 *     )
 *     생성된 Query
 */

@Entity
//@Table(uniqueConstraints = )
public class Member {

    @Id
    private Long id;

    /**
     *  - name : 필드와 매핑할 테이블의 컬럼 이름
     *  - insertable, updatable : 등록, 변경 가능 여부
     *  - unique : Unique 제약조건을 설정 할 떄 사용.
     *      - @Table 의 uniqueConstraints 속성과 역할이 같음.
     *      - Column에서 사용하기보다 @Table에서 사용하는것이 식별하기 좋음.
     *  - columnDefinition : 데이터베이스 컬럼 정보를 직접 줄 수 있다.
     *  - precision : 소수점을 포함한 전체 자릿수
     *  - scale : 소수의 자릿수
     *      - precision, scale 모두 double, float타임에는 적용되지 않음.
     *      - 정밀한 소수를 다루어야 할 때만 사용.
     */

    @Column(name = "name", updatable = false, columnDefinition = "varchar(100) default 'EMPTY'")
    private String username;

    @Column
    private BigDecimal age;

    /**
     * ORDINAL : Enum의 순서를 저장함. -> 위험. 사용하지 말 것.
     *  - roleType integer
     * STRING : Enum 의 이름을 데이터베이스에 저장.
     *  - roleType varchar(255)
     */
    @Enumerated(EnumType.ORDINAL)
    private RoleType roleType;

    /**
     * DATE : 날짜
     * TIME : 시간
     * TIMESTAMP : 날짜, 시간
     */
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createDate;
    //Java 8 이상에서는 아래와 같이 사용.
    private LocalDate createDate;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
    //Java 8 이상에서는 아래와 같이 사용.
    private LocalDateTime lastModifiedDate;


//    private LocalDateTime

    // varchar를 넘어서는 내용을 넣을 때, Lob을 사용한다.
    // 문자인경우 clob으로 생성
    @Lob
    private String description;


    /**
     * 특정 필드를 컬럼에서만 사용. 메모리에서만 사용.
     */
    @Transient
    private int temp;

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

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
