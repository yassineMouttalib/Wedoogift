package com.wedoogift.gift.entity;

import com.wedoogift.gift.interfaces.BalanceUserCalculator;
import com.wedoogift.gift.interfaces.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "mealCard")
public class MealCard implements Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long value_;
    private final LocalDate localDate = LocalDate.now();
    boolean expired = false;
    public MealCard(Long value_) {
        this.value_ = value_;
    }

    @Override
    public String toString() {
        return "MealCard{" +
                "id=" + id +
                ", value_=" + value_ +
                '}';
    }

    @Override
    public Long accept(BalanceUserCalculator balanceUser) {
        return balanceUser.calculate(this);
    }
}
