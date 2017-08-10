package com.shubin.entity;

/**
 * Created by vitaly on 07.08.17.
 */
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class JobRecord {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Date startDate;
    private Date stopDate;

    private Date lastExchange;

    @ManyToOne
    private Employee employee;


    @ManyToOne
    private Device device;
    private Long durability;
    private Boolean active;

    private Float difficult;
    private Float salaryRate;

}
