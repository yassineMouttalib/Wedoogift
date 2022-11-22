package com.wedoogift.gift.service;

import com.wedoogift.gift.entity.Company;
import com.wedoogift.gift.entity.GiftCard;
import com.wedoogift.gift.entity.MealCard;
import com.wedoogift.gift.entity.User;
import com.wedoogift.gift.exception.CompanyNotExistException;
import com.wedoogift.gift.exception.NotEnoughBalanceCompanyException;
import com.wedoogift.gift.exception.UserNotExistInCompanyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CompanyServiceTest {
    @Autowired
    CompanyService companyService;
    //Data
    public  Company getCompany() {
        System.out.println(" ---------------------");
        Company company = new Company("Tesla", 100L, 100L);
        User user1 = new User("user1");
        company.getUserList().add(user1);
        Company companyDB = companyService.addCompany(company);
        System.out.println(companyDB.toString());
        return companyDB;
    }

    @Test
    void distributeCard() throws UserNotExistInCompanyException, CompanyNotExistException, NotEnoughBalanceCompanyException {
        Company company = getCompany();
        User user1 =company.getUserList().get(0);
        Long ExpectedCompanyGiftBalance = 70L;
        Long ExpectedCompanyMealBalance = 50L;
        Long ExpectedUserGiftBalance = 30L;
        Long ExpectedUserMealBalance = 50L;
        companyService.distributeCard(new GiftCard(),company.getId(), user1.getId(), company.getGiftBalance() -ExpectedCompanyGiftBalance);
        companyService.distributeCard(new MealCard(),company.getId(), user1.getId(), company.getMealBalance() - ExpectedCompanyMealBalance);
        System.out.println("---------  company.getGiftBalance()"+company.getGiftBalance());
        System.out.println("---------  company.getMealBalance()"+company.getMealBalance());
        System.out.println(companyService.getCompany(company.getId()));
        Map<String,Long> userBalanceMap = companyService.calculateUsersBalance(company.getId());
        System.out.println(userBalanceMap);
        assertEquals(ExpectedCompanyGiftBalance, company.getGiftBalance());
        assertEquals(ExpectedCompanyMealBalance, company.getMealBalance());
        assertEquals(ExpectedUserGiftBalance, userBalanceMap.get(GiftCard.class.getSimpleName()));
        assertEquals(ExpectedUserMealBalance, userBalanceMap.get(MealCard.class.getSimpleName()));

    }
    @Test
    void distributeCardThrowException() {
        Company company = getCompany();
        Long giftValue = 170L;

        Assertions.assertThrows(NotEnoughBalanceCompanyException.class,
                () -> companyService.distributeCard(new GiftCard(), company.getId(), company.getUserList().get(0).getId(), giftValue));
    }
}