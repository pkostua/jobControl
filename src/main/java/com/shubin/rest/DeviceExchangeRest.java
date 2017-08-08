package com.shubin.rest;

import com.shubin.entity.Device;
import com.shubin.entity.Employee;
import com.shubin.entity.JobRecord;
import com.shubin.repository.DeviceRepository;
import com.shubin.repository.EmployeeRepository;
import com.shubin.repository.JobRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

/**
 * Created by vitaly on 08.08.17.
 */
@RestController
@RequestMapping("api/exchange")
public class DeviceExchangeRest {

    @Autowired
    JobRecordRepository jobRecordRepository;
    EmployeeRepository employeeRepository;
    DeviceRepository deviceRepository;


    @RequestMapping()
    @ResponseBody
    public String exchange(@RequestParam("cardid") String cardId, @RequestParam("deviceid") Long deviceId) {

        try{
            Device device = deviceRepository.findOne(deviceId);
            if (device == null) return "nde"; //new device error


            Employee employee = employeeRepository.findByCardId(cardId);
            if(employee == null){
                employee= new Employee();
                employee.setCardId(cardId);
                employee.setFirstName("Незарегистрированный работник");
                employee.setSalaryRate(0f);
                employee = employeeRepository.save(employee);

            }
            else {
                JobRecord existEpmloyeeJob = jobRecordRepository.findByActiveTrueAndEmployee(employee);
                if(existEpmloyeeJob != null)
                    stopJob(existEpmloyeeJob);

            }


            JobRecord jobRecord=jobRecordRepository.findByActiveTrueAndDevice(device);
            if (jobRecord != null) {
               if(Objects.equals(employee.getCardId(), jobRecord.getEmployee().getCardId())) {
                   jobRecord.setLastExchange(new Date());
               }
               else{
                   return "dbe "+ "Устройство занято " + jobRecord.getEmployee().getFirstName(); //device deasy error
               }

            }
            else{
                jobRecord=new JobRecord();
                jobRecord.setActive(true);
                jobRecord.setDevice(device);
                jobRecord.setEmployee(employee);
                jobRecord.setStartDate(new Date());
                jobRecord.setLastExchange(new Date());
            }

            Long sec = (new Date().getTime() - jobRecord.getStartDate().getTime())/1000;
            jobRecord.setDurability(sec);
            jobRecordRepository.save(jobRecord);

            Long min = sec/60;
            sec = sec%60;
            return jobRecord.getEmployee().getLastName()+" "+min+":"+sec;
        }
        catch (Exception ex){
            System.out.print("Ошибка старта работы " + ex);
            return "err Ошибка сервера";
        }
    }

    @RequestMapping("/newdevice")
    @ResponseBody
    public String newDevice() {
        Device device = new Device();
        device.setName("Новое устройство");
        device = deviceRepository.save(device);
        return device.getId()+"";

    }

    @RequestMapping("/stopJob")
    @ResponseBody
    public String stopJob(@RequestParam("cardid") String cardId, @RequestParam("deviceid") Long deviceId) {

        try{
            Device device = deviceRepository.findOne(deviceId);
            JobRecord jobRecord= jobRecordRepository.findByActiveTrueAndDevice(device);
            jobRecord = stopJob(jobRecord);
            Long min = jobRecord.getDurability()/60;
            Long sec = jobRecord.getDurability()%60;
            return "ok1 Завершено дл:"+min+":"+sec;
        }catch (Exception ex){
            return "err";
        }
    }

    JobRecord stopJob(JobRecord jobRecord){

        jobRecord.setLastExchange(new Date());
        jobRecord.setStopDate(new Date());
        jobRecord.setActive(false);
        Long sec = (new Date().getTime() - jobRecord.getStartDate().getTime())/1000;
        jobRecord.setDurability(sec);
        jobRecord = jobRecordRepository.save(jobRecord);
        return jobRecord;
    }
}
