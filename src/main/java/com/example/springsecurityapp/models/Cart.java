package com.example.springsecurityapp.models;

import jakarta.persistence.*;

@Entity
@Table(name="product_cart")
public class Cart { // Корзина
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "person_id")
    private int personId;
    @Column(name = "product_id")
    private int productId;

    public Cart(int person_id, int productId) {
        this.personId = person_id;
        this.productId = productId;
    }

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerson_id() {
        return personId;
    }

    public void setPerson_id(int person_id) {
        this.personId = person_id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
