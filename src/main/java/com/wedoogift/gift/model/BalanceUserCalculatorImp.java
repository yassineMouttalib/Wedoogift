package com.wedoogift.gift.model;

import com.wedoogift.gift.entity.GiftCard;
import com.wedoogift.gift.entity.MealCard;
import com.wedoogift.gift.interfaces.BalanceUserCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;

@Component
public class BalanceUserCalculatorImp implements BalanceUserCalculator {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${LimitDaysGift}")
    Long limitDaysGift;
    @Override
    public Long calculate(GiftCard giftCard) {
        LocalDate localDateLimit = giftCard.getLocalDate().plusDays(limitDaysGift);
        if (giftCard.isExpired()||giftCard.getLocalDate().isAfter(localDateLimit)) {
            giftCard.setValue_(0L);
            giftCard.setExpired(true);
            logger.info("giftCard expired"+ giftCard.isExpired());
        }

        return giftCard.getValue_();
    }

    @Override
    public Long calculate(MealCard mealCard) {
        int year =mealCard.getLocalDate().getYear()+1;
        int month = Month.FEBRUARY.getValue();
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        LocalDate localDateLimit = LocalDate.of(year,month,daysInMonth);
        if (mealCard.isExpired()||mealCard.getLocalDate().isAfter(localDateLimit)) {
            mealCard.setValue_(0L);
            mealCard.setExpired(true);
            logger.info("mealCard expired"+ mealCard.isExpired());
            logger.info("mealCard.getLocalDate() :"+ mealCard.getLocalDate());
            logger.info("localDateLimit :"+ localDateLimit);
        }

        return mealCard.getValue_();
    }
}
