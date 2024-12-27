package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        Employee savedEmployee = null;
        try {
            savedEmployee = employeeRepository.save(employee);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Save employee: " + savedEmployee);
        return savedEmployee;
    }

    public Employee getEmployeeById(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        } else {
            throw new RuntimeException("No employee is mapping with id");
        }
    }

    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        List<Employee> availableEmployees = new ArrayList<>();
        for (Employee employee : employeeRepository.findAllByDaysAvailableContains(date.getDayOfWeek()))
            if (employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        return availableEmployees;
    }

}
