package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = null;
        try {
            savedCustomer = customerRepository.save(customer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return savedCustomer;
    }

    public List<Customer> getAllCustomers() {

        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

}
