package fr.mika.tunit.employee;

import fr.mika.tunit.employee.dto.EmployeeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmployeeDTO> findAl() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        try {
            Optional<EmployeeDTO> employeeDTO = this.service.findById(id);
            return ResponseEntity.ok(employeeDTO.get());
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().header(e.getMessage()).build();
        }
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> save(@RequestBody EmployeeDTO toSave) {
        return ResponseEntity.ok(this.service.save(toSave));
    }

    @PutMapping
    public ResponseEntity<EmployeeDTO> update(@RequestBody EmployeeDTO toUpdate) {
        return ResponseEntity.ok(this.service.save(toUpdate));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete(@RequestBody EmployeeDTO toDelete) {
        this.service.delete(toDelete);
        return ResponseEntity.ok(true);
    }
}
