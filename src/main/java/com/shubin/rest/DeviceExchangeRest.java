package com.shubin.rest;

import com.shubin.entity.Device;
import com.shubin.entity.Employee;
import com.shubin.entity.JobRecord;
import com.shubin.repository.DeviceRepository;
import com.shubin.repository.EmployeeRepository;
import com.shubin.repository.JobRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by vitaly on 08.08.17.
 */
@RestController
@RequestMapping("api/exchange")
public class DeviceExchangeRest {


    private Logger log = LoggerFactory.getLogger(DeviceExchangeRest.class);
    @Autowired
    private JobRecordRepository jobRecordRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @RequestMapping()
    @ResponseBody
    public String exchange(@RequestParam("cardid") String cardId, @RequestParam("deviceid") Long deviceId) {

        boolean needToUpdateMonitor = false;
        try{
            Device device = deviceRepository.findOne(deviceId);
            if (device == null) return "nde"; //no device error

            Employee employee = employeeRepository.findByCardId(cardId);
            if(employee == null) employee=createNewEmployee(cardId);
            else {
                JobRecord existEpmloyeeJob = jobRecordRepository.findByActiveTrueAndEmployee(employee);
                if(existEpmloyeeJob != null) {
                    if(!Objects.equals(device.getId(), existEpmloyeeJob.getDevice().getId())) {
                        stopJob(existEpmloyeeJob);
                        needToUpdateMonitor = true;
                    }
                }
            }

            JobRecord jobRecord = jobRecordRepository.findByActiveTrueAndDevice(device);
            if (jobRecord != null) {
               if(Objects.equals(employee.getCardId(), jobRecord.getEmployee().getCardId())) {
                   jobRecord.setLastExchange(new Date());
               }
               else{
                   log.info("Попытка работы с занятым устройством");
                   return "dbe "+ "Устройство занято " + jobRecord.getEmployee().getFirstName();//device busy error}
               }
            }

            else{
                jobRecord = createNewJob(device,employee);
                needToUpdateMonitor=true;
            }

            Long sec = (new Date().getTime() - jobRecord.getStartDate().getTime())/1000;
            jobRecord.setDurability(sec);
            jobRecord = jobRecordRepository.save(jobRecord);

            if (needToUpdateMonitor) updateMonitor();

            Long min = sec/60;
            sec = sec%60;

            return jobRecord.getEmployee().getLastName()+" "+min+":"+sec;
        }
        catch (Exception ex){
            log.error("Ошибка старта работы " + ex);
            return "err Ошибка сервера";
        }
    }

    @RequestMapping("/newdevice")
    @ResponseBody
    public String newDevice() {
        Device device = new Device();
        device.setName("Новое устройство");
        device.setDifficult(1f);
        device = deviceRepository.save(device);
        updateMonitor();
        return device.getId()+"";

    }

    @RequestMapping("/stop")
    @ResponseBody
    public String stopJob(@RequestParam("deviceid") Long deviceId) {

        //try{
            Device device = deviceRepository.findOne(deviceId);
            JobRecord jobRecord= jobRecordRepository.findByActiveTrueAndDevice(device);
            jobRecord = stopJob(jobRecord);
            Long min = jobRecord.getDurability()/60;
            Long sec = jobRecord.getDurability()%60;
            updateMonitor();
            log.info("Работа "+jobRecord.getId()+ " остановлена рест сервисом");
            return "ok1 Завершено дл:"+min+":"+sec;
        //}catch (Exception ex){
         //   return "err  " + ex;
        //}
    }

    JobRecord stopJob(JobRecord jobRecord){

        jobRecord.setLastExchange(new Date());
        jobRecord.setStopDate(new Date());
        jobRecord.setActive(false);
        jobRecord.setSalaryRate(jobRecord.getEmployee().getSalaryRate());
        Long sec = (new Date().getTime() - jobRecord.getStartDate().getTime())/1000;
        jobRecord.setDurability(sec);
        jobRecord = jobRecordRepository.save(jobRecord);
        return jobRecord;
    }

    void updateMonitor (){
        List<Device> deviceList = deviceRepository.findAll();
        deviceList.forEach(device->{
            JobRecord jobRecord = jobRecordRepository.findByActiveTrueAndDevice(device);
            if (jobRecord != null)
                jobRecord.setDevice(null);
            device.setCurrentJob(jobRecord);
        });
        simpMessagingTemplate.convertAndSend("/topic/monitorUpdate", deviceList);

    }
    JobRecord createNewJob (Device device, Employee employee){
        JobRecord jobRecord=new JobRecord();
        jobRecord.setActive(true);
        jobRecord.setDevice(device);
        jobRecord.setEmployee(employee);
        jobRecord.setStartDate(new Date());
        jobRecord.setLastExchange(new Date());
        return jobRecord;
    }

    Employee createNewEmployee (String cardId){
        Employee employee= new Employee();
        employee.setCardId(cardId);
        employee.setFirstName("Незарегистрированный работник");
        employee.setSalaryRate(0f);
        employee = employeeRepository.save(employee);
        return employee;

    }
}
