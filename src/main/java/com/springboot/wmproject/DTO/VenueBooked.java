package com.springboot.wmproject.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


public class VenueBooked {

//   private Integer id;
   private String venueId;
   private String booked;

   public String getVenueId() {
      return venueId;
   }

   public void setVenueId(String venueId) {
      this.venueId = venueId;
   }

   public String getBooked() {
      return booked;
   }

   public void setBooked(String booked) {
      this.booked = booked;
   }

//   public Integer getId() {
//      return id;
//   }
//
//   public void setId(Integer id) {
//      this.id = id;
//   }
}
