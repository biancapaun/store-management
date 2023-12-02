package com.api.storemanagement.mapper;

import com.api.storemanagement.dto.PriceDTO;
import com.api.storemanagement.entities.Price;

public class PriceMapper {

    public static Price toEntity(PriceDTO priceDTO) {
        Price price = new Price();
        price.setId(priceDTO.getId());
        price.setValue(priceDTO.getValue());
        price.setStartDate(priceDTO.getStartDate());
        price.setEndDate(priceDTO.getEndDate());

        return price;
    }
    public static PriceDTO toDTO(Price price) {
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setId(price.getId());
        priceDTO.setValue(price.getValue());
        priceDTO.setStartDate(price.getStartDate());
        priceDTO.setEndDate(price.getEndDate());

        return priceDTO;
    }
}
