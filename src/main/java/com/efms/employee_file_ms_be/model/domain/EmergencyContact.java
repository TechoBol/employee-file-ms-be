package com.efms.employee_file_ms_be.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyContact {
    private String fullName;
    private String relation;
    private String phone;
    private String address;
}
