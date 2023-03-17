package wm.clientmvc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamSummaryDTO {
    private int team_id;
    private String team_name;
    private Integer total_members;
    private String leader_name;
    private int emp_id;

}
