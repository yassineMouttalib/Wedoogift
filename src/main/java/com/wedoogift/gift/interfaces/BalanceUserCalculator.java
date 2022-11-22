package com.wedoogift.gift.interfaces;

import com.wedoogift.gift.entity.GiftCard;
import com.wedoogift.gift.entity.MealCard;

public interface BalanceUserCalculator {
    Long calculate(GiftCard giftCard);
    Long calculate(MealCard mealCard);
}
