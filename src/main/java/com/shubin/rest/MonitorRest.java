package com.shubin.rest;

import com.shubin.entity.Device;
import com.shubin.entity.JobRecord;
import com.shubin.repository.DeviceRepository;
import com.shubin.repository.JobRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by vitaly on 09.08.17.
 */
@RestController
@RequestMapping("api/monitor")
public class MonitorRest {

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    JobRecordRepository jobRecordRepository;

    @RequestMapping()
    @ResponseBody
    public List<Device> getMonitor() {
        List<Device> deviceList = deviceRepository.findAll();
        deviceList.forEach(device->{
            JobRecord jobRecord = jobRecordRepository.findByActiveTrueAndDevice(device);
            if (jobRecord != null)
                jobRecord.setDevice(null);
            device.setCurrentJob(jobRecord);
        });
        return deviceList;

    }
}
