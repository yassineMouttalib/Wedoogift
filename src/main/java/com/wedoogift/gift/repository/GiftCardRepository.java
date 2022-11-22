package com.wedoogift.gift.repository;

import com.wedoogift.gift.entity.GiftCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
}
