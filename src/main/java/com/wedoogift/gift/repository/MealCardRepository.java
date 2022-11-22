package com.wedoogift.gift.repository;

import com.wedoogift.gift.entity.MealCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealCardRepository extends JpaRepository<MealCard, Long> {
}
