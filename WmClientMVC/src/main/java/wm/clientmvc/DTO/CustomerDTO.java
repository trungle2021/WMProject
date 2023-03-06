package wm.clientmvc.DTO;

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
    private String name;
    private String address;
    private String phone;
    private String gender;
    private String avatar;
    private Set<BookingDTO> bookings = new HashSet<>();
    private Set<OrderDTO> orders = new HashSet<>();

}
