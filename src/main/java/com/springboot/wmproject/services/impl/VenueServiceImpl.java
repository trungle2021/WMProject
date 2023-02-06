package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.entities.Venues;
import com.springboot.wmproject.repositories.CustomerRepository;
import com.springboot.wmproject.repositories.OrderRepository;
import com.springboot.wmproject.repositories.VenueRepository;
import com.springboot.wmproject.services.VenueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {
    private VenueRepository venueRepository;
    private ModelMapper modelMapper;
    private CustomerRepository customerRepository;
    private OrderRepository orderRepository;

    @Autowired
    public VenueServiceImpl(VenueRepository venueRepository, ModelMapper modelMapper, CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<VenueDTO> getAllVenue() {
        return venueRepository.findAll().stream().map(venue -> mapToDTO(venue)).collect(Collectors.toList());
    }

    @Override
    public List<VenueDTO> getAllVenueByCustomerId(Integer customerId) {
        return null;
    }

    @Override
    public List<VenueDTO> getAllVenueByOrderId(Integer orderId) {
        return venueRepository.getAllByOrderId(orderId).stream().map(venue -> mapToDTO(venue)).collect(Collectors.toList());

    }

    @Override
    public VenueDTO getOneVenue(int venueId) {
        return null;
    }

    @Override
    public VenueDTO createVenue(VenueDTO venueDTO) {
        return null;
    }

    @Override
    public VenueDTO updateVenue(VenueDTO venueDTO) {
        return null;
    }

    @Override
    public void deleteVenue(int venueId) {

    }

    public VenueDTO mapToDTO(Venues venue){
        VenueDTO venueDTO = modelMapper.map(venue,VenueDTO.class);
        return venueDTO;
    }

    public Venues mapToEntity(VenueDTO venueDTO){
        Venues venue = modelMapper.map(venueDTO,Venues.class);
        return venue;
    }
}
