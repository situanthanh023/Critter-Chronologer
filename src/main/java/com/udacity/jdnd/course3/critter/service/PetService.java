package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Pet savePet(Pet pet) {
        Customer owner = pet.getOwner();
        Pet savedPet = petRepository.save(pet);
        if (owner != null) {
            owner.getPets().add(savedPet);
            Customer customer = customerRepository.save(owner);
        }
        System.out.println("Add pet: " + savedPet);
        return savedPet;
    }

    public Pet findPetById(long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isPresent()) {
            return optionalPet.get();
        } else {
            throw new RuntimeException("No pet is mapping with id");
        }
    }

    public List<Pet> getAllPets() {
        List<Pet> allPets = null;
        try {
            allPets = petRepository.findAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return allPets;
    }

    public Customer getOwnerByPet(long petId) {
        Customer customer = null;
        try {
            customer = petRepository.getOne(petId).getOwner();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Get owner is: " + customer);
        return customer;
    }

}
