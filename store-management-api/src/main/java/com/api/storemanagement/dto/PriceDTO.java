package com.api.storemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class PriceDTO {

    private Integer id;
    private BigDecimal value;
    private Timestamp startDate;
    private Timestamp endDate;
}
