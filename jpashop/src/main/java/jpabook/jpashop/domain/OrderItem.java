package jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @Column(name = "ORDER_ID")
    private Long ordierId;

    @Column(name = "ITEM_ID")
    private Long itemId;

    private Integer orderPrice;

    private Integer count;
}
