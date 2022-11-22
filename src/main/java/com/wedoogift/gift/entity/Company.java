package com.wedoogift.gift.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private Long giftBalance;
    private Long mealBalance;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_company_use_id")
    List<User> userList = new ArrayList<>();
    public Company(String name, Long giftBalance, Long mealBalance) {
        this.name=name;
        this.giftBalance=giftBalance;
        this.mealBalance = mealBalance;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", giftBalance=" + giftBalance +
                ", mealBalance=" + mealBalance +
                ", userList=" + userList +
                '}';
    }
}
