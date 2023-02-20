package com.springboot.wmproject.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "venue_images", schema = "wmproject", catalog = "")
public class VenueImages {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "url", nullable = true, length = -1)
    private String url;
    @Basic
    @Column(name = "venue_id", nullable = true)
    private Integer venueId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Venues venuesByVenueId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VenueImages that = (VenueImages) o;
        return id == that.id && Objects.equals(url, that.url) && Objects.equals(venueId, that.venueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, venueId);
    }

    public Venues getVenuesByVenueId() {
        return venuesByVenueId;
    }

    public void setVenuesByVenueId(Venues venuesByVenueId) {
        this.venuesByVenueId = venuesByVenueId;
    }
}
