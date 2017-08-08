package com.shubin.rest;

import com.shubin.entity.Employee;
import com.shubin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vitaly on 07.08.17.
 */
@RestController
@RequestMapping("api/employee")
public class EmployeeRest {

    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping()
    @ResponseBody
    public Employee getById(@RequestParam("id") Long id) {
        Employee employee = employeeRepository.findOne(id);
        return employee;
    }
    @RequestMapping("/getAll")
    @ResponseBody
    public List<Employee> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save (@RequestBody Employee employee){
        employeeRepository.save(employee);
        return "OK";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam("id") Long id) {
        //Employee employee = employeeRepository.findOne(id);
        employeeRepository.delete(id);
        return "Ok";
    }
}
