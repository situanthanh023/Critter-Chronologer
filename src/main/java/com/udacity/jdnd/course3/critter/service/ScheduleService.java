package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetService petService;

    @Autowired
    EmployeeService employeeService;


    public Schedule createSchedule(Schedule schedule) {
        Schedule newSchedule = null;
        try {
            newSchedule = scheduleRepository.save(schedule);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return newSchedule;
    }

    public Employee setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee =  optionalEmployee.get();
            employee.setDaysAvailable(daysAvailable);
            return employeeRepository.save(employee);
        } else {
            throw new RuntimeException("No employee is mapping with id");        }
    }


    public List<Schedule> getAllSchedules() {
        List<Schedule> allSchedule = null;
        try {
            allSchedule = scheduleRepository.findAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allSchedule;
    }

    public List<Schedule> getScheduleForPet(long petId) {
        Pet pet = petService.findPetById(petId);
        return scheduleRepository.findAllByPetsContains(pet);
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        Employee employee = null;
        try {
        employee = employeeService.getEmployeeById(employeeId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return scheduleRepository.findAllByEmployeesContains(employee);

    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        List<Schedule> allSchedules = null;
        try {
            allSchedules= scheduleRepository.findAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        List<Schedule> customerSchedule = new ArrayList<>();
        for (Schedule s : allSchedules) {
            List<Pet> petList = s.getPets();
            petList.stream().filter(p -> p.getOwner().getId() == customerId).map(p -> s).forEach(customerSchedule::add);
        }
        return customerSchedule;
    }

}
