package com.shubin.repository;

import com.shubin.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vitaly on 07.08.17.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {


    Device findByMac(String mac);
}
