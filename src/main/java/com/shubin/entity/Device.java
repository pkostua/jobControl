package com.shubin.entity;

import javax.persistence.*;

import lombok.Data;

/**
 * Created by vitaly on 07.08.17.
 */
@Data
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String mac;
    private String ip;
    private Float difficult;
    private Long workTime;

    @Transient
    private JobRecord currentJob;
}
