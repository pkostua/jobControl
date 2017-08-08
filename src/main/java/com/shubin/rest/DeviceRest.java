package com.shubin.rest;

import com.shubin.entity.Device;
import com.shubin.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vitaly on 07.08.17.
 */
@RestController
@RequestMapping("api/device")
public class DeviceRest {

 @Autowired
    DeviceRepository deviceRepository;

    @RequestMapping()
    @ResponseBody
    public Device getById(@RequestParam("id") Long id) {
        Device device = deviceRepository.findOne(id);
        return device;

    }

    @RequestMapping("/getAll")
    @ResponseBody
    public List<Device> getAll() {
        List<Device> devices = deviceRepository.findAll();
        return devices;
    }
    @RequestMapping(method = RequestMethod.POST)
    public String save (@RequestBody Device device){
        deviceRepository.save(device);
        return "OK";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam("id") Long id) {
        deviceRepository.delete(id);
        return "Ok";
    }

}
