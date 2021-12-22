package fr.mika.tunit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mika.tunit.employee.Employee;
import fr.mika.tunit.employee.EmployeeController;
import fr.mika.tunit.employee.EmployeeService;
import fr.mika.tunit.employee.dto.EmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;


    @Test
    public void testFindAllEmployees() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    public void testSaveEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "titi", "titi@titi.org", new Date(),1, 1236.36f);
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(json.toJson(employeeDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testFindOneEmployeeWhereEmployeeNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/employees/1"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testFindOneEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "titi", "titi@titi.org", new Date(),1, 1236.36f);
        BDDMockito.given(service.findById(1L)).willReturn(Optional.of(employeeDTO));
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/employees/1"))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn();
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        EmployeeDTO resultEmployeeDTO = json.fromJson(result.getResponse().getContentAsString(), EmployeeDTO.class);
        Assertions.assertEquals(resultEmployeeDTO, employeeDTO);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        // Create a mock employee
        EmployeeDTO savedEmployee = new EmployeeDTO(1L, "titi", "titi@titi.org", new Date(),1, 1236.36f);
        BDDMockito.given(service.findById(1L)).willReturn(Optional.of(savedEmployee));
        MvcResult result1 = this.mockMvc.perform(MockMvcRequestBuilders.get("/employees/1"))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn();
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        EmployeeDTO oldEmployeeDTO = json.fromJson(result1.getResponse().getContentAsString(), EmployeeDTO.class);

        // Updated the mock employee
        oldEmployeeDTO.setUsername("Pipirupi");
        BDDMockito.when(service.save(any(EmployeeDTO.class))).thenReturn(oldEmployeeDTO);


        // Check PUT Request
        MvcResult result2 = this.mockMvc.perform(MockMvcRequestBuilders.put("/employees")
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .content(json.toJson(oldEmployeeDTO)))
                                        .andExpect(MockMvcResultMatchers.status().isCreated())
                                        .andReturn();
        EmployeeDTO newEmployeeDTO = json.fromJson(result2.getResponse().getContentAsString(), EmployeeDTO.class);

        // Assert
        Assertions.assertEquals(oldEmployeeDTO.getUsername(), newEmployeeDTO.getUsername());
    }

    @Test
    public void testDeletEmployee() throws Exception {
        EmployeeDTO toDelete = new EmployeeDTO(1L, "titi", "titi@titi.org", new Date(),1, 1236.36f);
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.delete("/employees")
                                                                        .contentType(MediaType.APPLICATION_JSON)
                                                                        .content(json.toJson(toDelete)))
                                        .andExpect(MockMvcResultMatchers.status().isOk())
                                        .andReturn();
    }
}
