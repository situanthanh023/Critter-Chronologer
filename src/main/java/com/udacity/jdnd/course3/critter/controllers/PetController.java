package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {

        Pet pet = null;
        try {
            pet = petService.savePet(convertPetDtoToPet(petDTO));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return convertPettoPetDTO(pet);
    }


    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        PetDTO petDTO = null;
        try {
            petDTO = convertPettoPetDTO(petService.findPetById(petId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return petDTO;
    }

    @GetMapping
    public List<PetDTO> getPets() {
        List<PetDTO> allPetDTO = new ArrayList<>();
        try {
            List<Pet> pets = petService.getAllPets();
            pets.forEach(pet -> allPetDTO.add(convertPettoPetDTO(pet)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return allPetDTO;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> allPetDTO = new ArrayList<>();
        List<Pet> allPets = null;
        try {
            allPets = petService.getAllPets();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        allPets.forEach(p -> allPetDTO.add(convertPettoPetDTO(p)));
        return allPetDTO;
    }

    private PetDTO convertPettoPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO = modelMapper.map(pet, PetDTO.class);
        return petDTO;
    }

    private Pet convertPetDtoToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet = modelMapper.map(petDTO, Pet.class);
        return pet;
    }
}
