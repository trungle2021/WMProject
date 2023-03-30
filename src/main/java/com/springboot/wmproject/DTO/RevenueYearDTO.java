package com.springboot.wmproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueYearDTO {
    private List<RevenueMonthDTO> revenueMonthList;
    private Double totalRevenueYear;




}
