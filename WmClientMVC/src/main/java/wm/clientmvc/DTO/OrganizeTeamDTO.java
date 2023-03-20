package wm.clientmvc.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import wm.clientmvc.DTO.ValidateCustom.TeamName;

import java.util.List;

@Data
public class OrganizeTeamDTO {
    private int id;
    @TeamName
    private String teamName;
    private boolean is_deleted;
}
