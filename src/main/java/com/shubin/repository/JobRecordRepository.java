package com.shubin.repository;

import com.shubin.entity.Device;
import com.shubin.entity.Employee;
import com.shubin.entity.JobRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by vitaly on 07.08.17.
 */
public interface JobRecordRepository extends JpaRepository<JobRecord, Long> {

    List<JobRecord> findByEmployee (Employee employee);
    List<JobRecord> findByDevice (Device device);
    List<JobRecord> findByEmployeeAndStartDateAfterAndStopDateBefore (Employee employee, Date startDate, Date stopDate);
    List<JobRecord> findByStartDateAfterAndStopDateBefore (Date startDate, Date stopDate);
    List<JobRecord> findByDeviceAndStartDateAfterAndStopDateBefore (Device device, Date startDate, Date stopDate);
    JobRecord findByActiveTrueAndDevice(Device device);
    JobRecord findByActiveTrueAndEmployee(Employee employee);
    List<JobRecord> findByActiveTrue();
}
