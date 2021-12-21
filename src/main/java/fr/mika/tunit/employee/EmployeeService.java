package fr.mika.tunit.employee;

import fr.mika.tunit.employee.dto.EmployeeDTO;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EmployeeService {
    private EmployeeRepository repository;
    private ModelMapper mapper;

    public EmployeeService(EmployeeRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Return a list of employees
     * @return List<Employee>
     */
    public List<EmployeeDTO> findAll() {
        List employeesDTO = new ArrayList();
        this.repository.findAll()
                        .forEach(e -> employeesDTO.add(this.mapper.map(e, EmployeeDTO.class)));
        return employeesDTO;
    }

    /**
     * Find an employee by its id in the db
     * @param id Employee's id
     * @return Optional<EmployeeDTO>
     */
    public Optional<EmployeeDTO> findById(final Long id) throws NoSuchElementException {
        Optional<Employee> employee = this.repository.findById(id);
        return Optional.of(this.mapper.map(employee.get(), EmployeeDTO.class));
    }

    /**
     * Persist an employee in the DB
     * @param toSave The employee to save
     * @return EmployeeDTO
     */
    public EmployeeDTO save(EmployeeDTO toSave) {
        Employee employee = this.repository.save(this.mapper.map(toSave, Employee.class));
        return this.mapper.map(employee, EmployeeDTO.class);
    }

    /**
     * Delete an employee in the DB
     * @param toDelete The employee to delete
     */
    public void delete(EmployeeDTO toDelete) {
        this.repository.delete(this.mapper.map(toDelete, Employee.class));
    }
}
