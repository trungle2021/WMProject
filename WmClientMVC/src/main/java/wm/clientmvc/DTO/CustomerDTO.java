package wm.clientmvc.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private int id;
    private String first_name;
    private String last_name;
    private String address;
    private String phone;
    private String email;
    private String gender;
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private String avatar;
    private String avatarFromDB;
    private boolean is_verified;
    public String getAvatar() {
        return this.avatar != null ? this.avatar : this.avatarFromDB;
    }
//    private Set<BookingDTO> bookings = new HashSet<>();
//    private Set<OrderDTO> orders = new HashSet<>();

}
