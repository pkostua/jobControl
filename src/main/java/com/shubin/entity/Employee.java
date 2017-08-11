package com.shubin.entity;

import javax.persistence.*;

import lombok.Data;

import java.util.List;

/**
 * Created by vitaly on 07.08.17.
 */

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String description;
    private String cardId;

    @OneToMany
    private List<Device> allowDevices;

    @Transient
    private JobRecord currentJob;
    private Float salaryRate;


}
