package wm.clientmvc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderIn3MonthDTO {
    private List<OrderInMonthDTO> orderIn3Month;
    private Integer totalOrderCompleted;
    private Integer totalOrderCanceled;
    private Integer totalOrderRefunded;
    private Integer totalOrderUncompleted;

}
