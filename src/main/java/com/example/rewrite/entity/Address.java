package com.example.rewrite.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ADDRESS")
public class Address {

    @Id
    @Column(name = "address_id", nullable = false, length = 255)
    private String addressId; // 주소 ID (기본키)

    @Column(name = "address", nullable = true, length = 255)
    private String address; // 주소

    @Column(name = "uid", nullable = true, length = 255)
    private String uid; // 사용자 ID (외래키)

    @Column(name = "addressAlias", nullable = true, length = 255)
    private String addressAlias; // 주소 별칭

    @Column(name = "recipient", nullable = true, length = 255)
    private String recipient; // 수령인

    @Column(name = "createdAt", nullable = true, length = 255)
    private String createdAt; // 등록인

    @Column(name = "isDefault", nullable = true, length = 255)
    private String isDefault; // 기본 주소 여부

    public Address(String addressId, String address, String uid, String addressAlias, String recipient, String createdAt, String isDefault) {
        this.addressId = addressId;
        this.address = address;
        this.uid = uid;
        this.addressAlias = addressAlias;
        this.recipient = recipient;
        this.createdAt = createdAt;
        this.isDefault = isDefault;
    }
}
