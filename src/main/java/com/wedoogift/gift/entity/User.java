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
@Table(name = "user_")
public class User {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_mealCard_id")
    List<MealCard> mealCards = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_giftCard_id")
    List<GiftCard> giftCards = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mealCards=" + mealCards +
                ", giftCards=" + giftCards +
                '}';
    }
}
