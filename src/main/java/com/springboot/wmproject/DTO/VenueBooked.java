package com.springboot.wmproject.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


public class VenueBooked {

    //   private String bookedDay;
    private String venueId;
    private String bookedTime;

//   public String getBookedDay() {
//      return bookedDay;
//   }
//
//   public void setBookedDay(String bookedDay) {
//      this.bookedDay = bookedDay;
//   }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(String bookedTime) {
        this.bookedTime = bookedTime;
    }
}
