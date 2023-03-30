package wm.clientmvc.DTO;

import lombok.*;


@Data
public class ReviewDTO {
    private int id;
    private String content;
    private String datePost;
    private double rating;
    private boolean active;
    private Integer customerAccountId;
    private CustomerAccountDTO reviewByCustomerAccountId;
}
