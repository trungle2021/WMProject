package wm.clientmvc.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoodImageDTO {
    private int id;
    private String url;
    private int foodId;
}
