package jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order orders;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.ORDINAL)
    private DeliveryStatus status;
}
