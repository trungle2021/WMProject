package wm.clientmvc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInMonthDTO {
    public Integer year;
    public Integer month;
    public Integer countOrderCompleted;
    public Integer countOrderUnCompleted;
    public Integer countOrderCancel;
    public Integer countOrderRefund;
}
