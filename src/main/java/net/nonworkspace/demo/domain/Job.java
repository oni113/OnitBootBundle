package net.nonworkspace.demo.domain;

import lombok.Data;

@Data
public class Job {

    private Long id;

    private String title;

    private String description;

    private String location;

    private String salary;

    private Company compay;
}
