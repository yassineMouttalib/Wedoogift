package com.wedoogift.gift.service;

import com.wedoogift.gift.entity.Company;
import com.wedoogift.gift.entity.GiftCard;
import com.wedoogift.gift.entity.MealCard;
import com.wedoogift.gift.entity.User;
import com.wedoogift.gift.exception.CompanyNotExistException;
import com.wedoogift.gift.exception.NotEnoughBalanceCompanyException;
import com.wedoogift.gift.exception.UserNotExistInCompanyException;
import com.wedoogift.gift.interfaces.Card;
import com.wedoogift.gift.model.BalanceUserCalculatorImp;
import com.wedoogift.gift.repository.CompanyRepository;
import com.wedoogift.gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CompanyService {
    CompanyRepository companyRepository;
    UserRepository userRepository;
    BalanceUserCalculatorImp balanceUserCalculator;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository, BalanceUserCalculatorImp balanceUserCalculator) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.balanceUserCalculator =balanceUserCalculator;
    }
    public Optional<Company> getCompany(Long companyId) {
        if (companyId <1) {
            throw new IllegalArgumentException();
        }
        return companyRepository.findById(companyId);
    }
    public Company addCompany(Company company) {
        if (company == null) {
            throw new IllegalArgumentException();
        }
        return companyRepository.save(company);
    }
    //distribute a card to a user identified with userId of company identified with id companyId
    public User distributeCard(Card card, Long companyId, Long userId, Long amount) throws UserNotExistInCompanyException, CompanyNotExistException, NotEnoughBalanceCompanyException {
        if (amount < 1) throw new IllegalArgumentException("amount : " + amount);
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isEmpty()) {
            throw new CompanyNotExistException("companyId id  : " + companyId);
        }
        Optional<User> userOpt = companyOpt.get().getUserList().stream().filter(u -> u.getId().equals(userId)).findFirst();
        if (userOpt.isEmpty()) {
            throw new UserNotExistInCompanyException("user id  : " + userId);
        }

        return addCardToUserAndSaveUser(card, userOpt, companyOpt, amount);
    }
    // return map with Card class simple name as key and the amount of card type that users has, as value.
    public Map<String,Long> calculateUsersBalance(Long companyId) throws CompanyNotExistException {
        Map<String, Long> map = new HashMap<>();
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isEmpty()) {
            throw new CompanyNotExistException("companyId id  : " + companyId);
        }
        for (User user : companyOpt.get().getUserList()) {
            Long s1 = user.getGiftCards().stream().reduce(0L, (a,g) ->a +g.accept(balanceUserCalculator),Long::sum);
            map.put(GiftCard.class.getSimpleName(), s1);
            Long s2 = user.getMealCards().stream().reduce(0L, (a,g) ->a +g.accept(balanceUserCalculator),Long::sum);
            map.put(MealCard.class.getSimpleName(), s2);
        }
        return map;
    }

    // Tools
    private User addCardToUserAndSaveUser(Card card, Optional<User> userOpt, Optional<Company> companyOpt, Long amount) throws NotEnoughBalanceCompanyException, UserNotExistInCompanyException {
        if(userOpt.isEmpty()) throw new UserNotExistInCompanyException("addCardToUserAndSaveUser");
        User user = userOpt.get();
        Company company = companyOpt.get();
        if (card instanceof GiftCard) {
            if (companyOpt.get().getGiftBalance() < amount) {
                throw new NotEnoughBalanceCompanyException("balance : " + companyOpt.get().getGiftBalance());
            }
            company.setGiftBalance(company.getGiftBalance() - amount);
            user.getGiftCards().add(new GiftCard(amount));
        }
        if (card instanceof MealCard) {
            if (companyOpt.get().getMealBalance() < amount) {
                throw new NotEnoughBalanceCompanyException("balance : " + companyOpt.get().getMealBalance());
            }
            company.setMealBalance(company.getMealBalance() - amount);
            user.getMealCards().add(new MealCard(amount));
        }

        return userRepository.save(user);
    }
}
