package net.javaguides.springboot.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.module.ResolutionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
//@AllArgsConstructor
public class EmployeeController {

    private EmployeeRepository employeeRepository;
//    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // get employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return this.employeeRepository.findAll();
    }

    // get employee by id
    @GetMapping("/employees/{id1}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id1") Long employeeId)
            throws  ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id : : " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    // save employee
    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return this.employeeRepository.save(employee);
    }

    // update employee
    @PostMapping("/employees/{id2}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id2") Long employeeId,
                                                   @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id : : " + employeeId));

        employee.setEmail(employeeDetails.getEmail());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());

        return ResponseEntity.ok(this.employeeRepository.save(employee));
    }

    // delete employee
    @PostMapping("/employees/{id3}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id3") Long employeeId){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResolutionException("Employee not found for this id : :" + employeeId));

        this.employeeRepository.delete(employee);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

//    @GetMapping("/test")
//    @ApiOperation("Тест запрос")
//    public ResponseEntity<?> getAllPaginated() {
//        return new ResponseEntity<>("Hello",HttpStatus.OK);
//    }

//    @GetMapping("/all")
//    @ApiOperation("Получить список сотрудников")
//    public ResponseEntity<?> getAllPaginated(@RequestParam Optional<Integer> page,
//                                             @RequestParam Optional<Integer> size,
//                                             @RequestParam Optional<String[]> sortBy,
//                                             @RequestParam Optional<String> search) {
//        return buildResponse(employeeService.getAll(search,page,size,sortBy), HttpStatus.OK);
//    }

    @Autowired
    private EmployeeService service;

    @GetMapping("/test")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam Integer pageNo,
                                                          @RequestParam Integer pageSize,
                                                          @RequestParam String sortBy) {
        List<Employee> list = service.getAllEmployees(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Employee>>(list, new HttpHeaders(), HttpStatus.OK);
    }

}
