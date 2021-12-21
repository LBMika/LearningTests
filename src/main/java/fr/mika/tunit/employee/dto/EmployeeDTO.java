package fr.mika.tunit.employee.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDTO {
    private Long id;
    private String username;
    private String email;
    private Date birthdate;
    private Integer gender;
    private Float salary;
}
