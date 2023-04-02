package wm.clientmvc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueMonthDTO {
    public Integer year;
    public Integer month;
    public Double revenueInMonth;

}
