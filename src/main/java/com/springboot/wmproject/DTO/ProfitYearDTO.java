package com.springboot.wmproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfitYearDTO {
    List<ProfitMonthDTO> profitMonthList;
    Double totalProfitInYear;

}
