package com.wedoogift.gift;

import com.wedoogift.gift.entity.Company;
import com.wedoogift.gift.entity.GiftCard;
import com.wedoogift.gift.entity.MealCard;
import com.wedoogift.gift.entity.User;
import com.wedoogift.gift.repository.CompanyRepository;
import com.wedoogift.gift.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Commands implements CommandLineRunner {
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(" ---------------------");
        Company company = new Company("Tesla", 100L, 100L);
        User user1 =new User("user1");
        company.getUserList().add(user1);
        Company companyDB = companyRepository.save(company);
        System.out.println(companyDB);
        companyService.distributeCard(new GiftCard(),company.getId(), user1.getId(), 30L);
        companyService.distributeCard(new MealCard(),company.getId(), user1.getId(), 50L);
        System.out.println("---------  company.getGiftBalance()"+company.getGiftBalance());
        System.out.println("---------  company.getMealBalance()"+company.getMealBalance());
        System.out.println(companyRepository.findById(company.getId()));
        System.out.println(companyService.calculateUsersBalance(company.getId()));
    }
}
