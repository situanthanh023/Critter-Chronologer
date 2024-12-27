package com.udacity.jdnd.course3.critter.controllers;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.saveCustomer(convertCustomerDtoToCustomer(customerDTO));
        CustomerDTO theCustomerDTO = convertCustomerToCustomerDTO(customer);
        return theCustomerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> allCustomersDTO = new ArrayList<>();
        List<Customer> allCustomers = customerService.getAllCustomers();
        allCustomers.forEach(c -> allCustomersDTO.add(convertCustomerToCustomerDTO(c)));
        return allCustomersDTO;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        Customer customer = petService.getOwnerByPet(petId);
        return convertCustomerToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDtoToEmployee(employeeDTO);
        employee = employeeService.saveEmployee(employee);
        return convertEmployeetoEmployeeDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeetoEmployeeDTO(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public EmployeeDTO setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        return convertEmployeetoEmployeeDTO(scheduleService.setAvailability(daysAvailable, employeeId));
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeesDTO = new ArrayList<>();
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO.getDate(), employeeDTO.getSkills());
        employees.forEach(e -> employeesDTO.add(convertEmployeetoEmployeeDTO(e)));
        return employeesDTO;
    }


    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO = modelMapper.map(customer, CustomerDTO.class);
        List<Long> petIds = new ArrayList<>();
        if (customer.getPets() != null)
            petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }


    private Customer convertCustomerDtoToCustomer(CustomerDTO customerDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Customer customer = new Customer();
        customer = modelMapper.map(customerDTO, Customer.class);
        return customer;
    }


    private EmployeeDTO convertEmployeetoEmployeeDTO(Employee employee) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        return employeeDTO;
    }

    private Employee convertEmployeeDtoToEmployee(EmployeeDTO employeeDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Employee employee = new Employee();
        employee = modelMapper.map(employeeDTO, Employee.class);
        return employee;
    }

}
