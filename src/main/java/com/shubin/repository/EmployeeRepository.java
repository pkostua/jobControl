package com.shubin.repository;

import com.shubin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by vitaly on 07.08.17.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByCardId(String cardId);

}
