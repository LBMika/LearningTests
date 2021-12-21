package fr.mika.tunit.employee;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeConfiguration {
    @Bean
    public EmployeeService employeeService(EmployeeRepository repository, ModelMapper mapper) {
        return new EmployeeService(repository, mapper);
    }
}
