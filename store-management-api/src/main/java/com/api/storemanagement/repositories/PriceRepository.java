package com.api.storemanagement.repositories;

import com.api.storemanagement.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {
}
