package com.shubin.rest;

import com.shubin.entity.Device;
import com.shubin.entity.Employee;
import com.shubin.entity.JobRecord;
import com.shubin.repository.DeviceRepository;
import com.shubin.repository.EmployeeRepository;
import com.shubin.repository.JobRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by vitaly on 07.08.17.
 */
@RestController
@RequestMapping("api/job")
public class JobRecordRest {
    @Autowired
    JobRecordRepository jobRecordRepository;
    EmployeeRepository employeeRepository;
    DeviceRepository deviceRepository;

    @RequestMapping()
    @ResponseBody
    public JobRecord getById(@RequestParam("id") Long id) {
        JobRecord jobRecord = jobRecordRepository.findOne(id);
        return jobRecord;
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public List<JobRecord> getAll() {
        List<JobRecord> jobRecord = jobRecordRepository.findAll();
        return jobRecord;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save (@RequestBody JobRecord jobRecord){
        jobRecordRepository.save(jobRecord);
        return "OK";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam("id") Long id) {
        jobRecordRepository.delete(id);
        return "Ok";
    }

    @RequestMapping("/getByEmployeeId")
    @ResponseBody
    public List<JobRecord> getByEmployeeId(@RequestParam("employeeid") Long employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        List<JobRecord> jobRecords = jobRecordRepository.findByEmployee(employee);
        return jobRecords;
    }

    @RequestMapping("/getByEmployeeIdAndDate")
    @ResponseBody
    public List<JobRecord> getByEmployeeIdAndDate(@RequestParam("employeeid") Long employeeId,@RequestParam("startdate") Long start, @RequestParam("stopdate") Long stop) {

        Employee employee = employeeRepository.findOne(employeeId);
        Date startDate = new Date(start);
        Date stopDate = new Date (stop);
        List<JobRecord> jobRecords = jobRecordRepository.findByEmployeeAndStartDateAfterAndStopDateBefore(employee,startDate,stopDate);
        return jobRecords;
    }

    @RequestMapping("/getByDeviceId")
    @ResponseBody
    public List<JobRecord> getByDeviceId(@RequestParam("deviceid") Long deviceId) {
        Device device = deviceRepository.findOne(deviceId);
        List<JobRecord> jobRecords = jobRecordRepository.findByDevice(device);
        return jobRecords;
    }

    @RequestMapping("/getByDeviceIdAndDate")
    @ResponseBody
    public List<JobRecord> getByDeviceIdAndDate(@RequestParam("deviceid") Long deviceId,@RequestParam("startdate") Long start, @RequestParam("stopdate") Long stop) {

        Device device = deviceRepository.findOne(deviceId);
        Date startDate = new Date(start);
        Date stopDate = new Date (stop);
        List<JobRecord> jobRecords = jobRecordRepository.findByDeviceAndStartDateAfterAndStopDateBefore(device,startDate,stopDate);
        return jobRecords;
    }

    @RequestMapping("api/getActive")
    @ResponseBody
    public List<JobRecord> getActive() {

        List<JobRecord> jobRecords = jobRecordRepository.findByActiveTrue();
        return jobRecords;
    }
}
