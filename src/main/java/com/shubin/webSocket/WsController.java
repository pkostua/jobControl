package com.shubin.webSocket;

import com.shubin.entity.Device;
import com.shubin.repository.DeviceRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;

import java.util.List;


/**
 * Created by vitaly on 08.08.17.
 */
@Controller
public class WsController {

    private Logger log = LoggerFactory.getLogger(WsController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/monitorUpdate")
    public void monitorMessage(List<Device> deviceList){
        log.info("message.monitorUpdate");
    }

}
