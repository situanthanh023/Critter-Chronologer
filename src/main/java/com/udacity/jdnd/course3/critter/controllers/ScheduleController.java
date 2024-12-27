package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.createSchedule(convertScheduleDtoToSchedule(scheduleDTO));
        return convertScheduleToScheduleDTO(schedule);
    }


    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> allSchedule = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        allSchedule.forEach(schedule -> scheduleDTOS.add(convertScheduleToScheduleDTO(schedule)));
        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedulList = scheduleService.getScheduleForPet(petId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        schedulList.forEach(s -> scheduleDTOS.add(convertScheduleToScheduleDTO(s)));
        return scheduleDTOS;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> list = scheduleService.getScheduleForEmployee(employeeId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule s : list) scheduleDTOS.add(convertScheduleToScheduleDTO(s));
        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> customerSchedule = scheduleService.getScheduleForCustomer(customerId);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        customerSchedule.forEach(s -> scheduleDTOS.add(convertScheduleToScheduleDTO(s)));

        return scheduleDTOS;
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO = modelMapper.map(schedule, ScheduleDTO.class);
        List<Long> employeeIdList = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petIdList = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIdList);
        scheduleDTO.setPetIds(petIdList);
        return scheduleDTO;
    }


    private Schedule convertScheduleDtoToSchedule(ScheduleDTO scheduleDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Schedule schedule = new Schedule();
        schedule = modelMapper.map(scheduleDTO, Schedule.class);
        List<Pet> petList = scheduleDTO.getPetIds().stream().map(id -> petService.findPetById(id)).collect(Collectors.toList());
        List<Employee> employeeList = scheduleDTO.getEmployeeIds().stream().map(id -> employeeService.getEmployeeById(id)).collect(Collectors.toList());
        schedule.setEmployees(employeeList);
        schedule.setPets(petList);
        return schedule;
    }
}
