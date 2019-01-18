package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//DTYPE
@DiscriminatorColumn
/**
 *  create table Item (
 *        DTYPE varchar(31) not null,
 *         id bigint not null,
 *         name varchar(255),
 *         price integer not null,
 *         primary key (id)
 *     )
 *  DTYPE이 추가됨.
 *  DTYPE이라고 쓰는게 관례.
 */

/**
 * create table Item (
 *        DTYPE varchar(31) not null,
 *         id bigint not null,
 *         name varchar(255),
 *         price integer not null,
 *         artist varchar(255),
 *         author varchar(255),
 *         isbn varchar(255),
 *         actor varchar(255),
 *         director varchar(255),
 *         primary key (id)
 *     )
 *
 *  단일 테이블 전략. 성능상 이점이 있음.
 */
public abstract class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;

}