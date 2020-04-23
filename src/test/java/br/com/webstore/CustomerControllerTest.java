package br.com.webstore;

import br.com.webstore.Repository.CustomerRepository;
import br.com.webstore.Repository.DBUserRepository;
import br.com.webstore.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Project: api-webstore
 *
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 23/04/2020
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private DBUserRepository dbUserRepository;

    public CustomerControllerTest() {
    }

    @TestConfiguration
    static class Config {
        @Bean
        public RestTemplateBuilder restTemplateBuilder () {
            return new RestTemplateBuilder().basicAuthentication("lucas", "lucaskano");
        }
    }

    @Test
    public void whenListCustomerUsingIncorrectUsernameAndPassword_thenReturnStatusCode401Unauthorized () {
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/v1/admin/customers/", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void whenGetCustomerByIdUsingIncorrectUsernameAndPassword_thenReturnStatusCode401Unauthorized () {
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/v1/admin/customers/1", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void whenFindCustomerByNameUsingIncorrectUsernameAndPassword_thenReturnStatusCode401Unauthorized () {
        restTemplate = restTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/v1/admin/customers/findByName/customerName", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void whenSaveCustomerUsingIncorrectUsernameAndPassword_thenReturnResourceAccessException () {
        restTemplate = restTemplate.withBasicAuth("1", "1");

        Customer customer = new Customer
                .Builder()
                .name("Lucas1")
                .documentNumber("123156789011")
                .dateOfBirth("06-03-1994")
                .email("lucaskano@email.com")
                .phoneNumber("11948729032")
                .build();

        assertThrows(
                ResourceAccessException.class,
                () -> restTemplate.postForEntity("http://localhost:8080/v1/protected/customers/", customer, String.class));
    }

    @Test
    public void whenDeleteCustomerUsingIncorrectUsernameAndPassword_thenReturnStatusCode401Unauthorized () {
        restTemplate = restTemplate.withBasicAuth("1", "1");

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/{id}", DELETE, null, String.class, 1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void whenUpdateCustomerUsingIncorrectUsernameAndPassword_thenReturnResourceAccessException () {
        restTemplate = restTemplate.withBasicAuth("1", "1");

        Customer customer = new Customer
                .Builder()
                .name("Lucas2")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucas@email.com")
                .phoneNumber("11948729034")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

        assertThrows(
                ResourceAccessException.class,
                () -> restTemplate.exchange("http://localhost:8080/v1/admin/customers", PUT, entity, String.class, 1L));
    }


    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"USER"})
    public void whenListAllCustomersUsingCorrectRole_thenReturnStatusCode200 () throws Exception {
        List<Customer> customers = asList(new Customer
                .Builder()
                .name("Lucas Tres")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucas@email.com")
                .phoneNumber("11948729034")
                .build(),
                new Customer
                        .Builder()
                        .name("Lucas2")
                        .documentNumber("123456689012")
                        .dateOfBirth("06-03-1995")
                        .email("lucas2@email.com")
                        .phoneNumber("11948728033")
                        .build());

        PageImpl pagedCustomers = new PageImpl(customers);

        when(customerRepository.findAll(isA(Pageable.class))).thenReturn(pagedCustomers);

        mockMvc.perform(get("http://localhost:8080/v1/admin/customers/"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(customerRepository).findAll(isA(Pageable.class));
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"USER"})
    public void whenGetCustomerByIdUsingCorrectRoleAndCustomerDoesntExist_thenReturnStatusCode404 () throws Exception {
        Customer customer = new Customer
                .Builder()
                .name("Lucas3")
                .documentNumber("123456789012")
                .dateOfBirth("06-03-1999")
                .email("lucas2@email.com")
                .phoneNumber("11948729033")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(get("http://localhost:8080/v1/admin/customers/{id}", 6))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(customerRepository).findById(6L);
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"USER"})
    public void whenFindCustomersByNameUsingCorrectRole_thenReturnStatusCode200 () throws Exception {
        List<Customer> customers = asList(new Customer
                .Builder()
                .name("UsuarioTeste1")
                .documentNumber("123456789012")
                .dateOfBirth("06-03-1999")
                .email("teste1@email.com")
                .phoneNumber("11948729033")
                .build(),
                new Customer
                        .Builder()
                        .name("UsuarioTeste2")
                        .documentNumber("123456789012")
                        .dateOfBirth("06-03-1999")
                        .email("teste2@email.com")
                        .phoneNumber("11948729033")
                        .build(),
        new Customer
                .Builder()
                .name("UsuarioTeste3")
                .documentNumber("123456789012")
                .dateOfBirth("06-03-1999")
                .email("teste3@email.com")
                .phoneNumber("11948729033")
                .build());

        when(customerRepository.findByNameIgnoreCaseContaining("usuario")).thenReturn(customers);

        mockMvc.perform(get("http://localhost:8080/v1/admin/customers/findByName/usuario"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(customerRepository).findByNameIgnoreCaseContaining("usuario");
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenDeleteUsingCorrectRole_thenReturnStatusCode200 () throws Exception {

        Customer customer = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucas@email.com")
                .phoneNumber("11948729034")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));

        doNothing().when(customerRepository).deleteById(customer.getId());

        mockMvc.perform(delete("http://localhost:8080/v1/admin/customers/{id}", 3))
                .andExpect(status().isOk())
                .andDo(print());

        verify(customerRepository).deleteById(customer.getId());
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenDeleteHasRoleAdminAndCustomerDontExist_thenReturnStatusCode404 () throws Exception {

        doNothing().when(customerRepository).deleteById(3L);

        mockMvc.perform(delete("http://localhost:8080/v1/admin/customers/{id}", 1))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(customerRepository, atLeast(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"USER"})
    public void whenDeleteHasRoleUser_thenReturnStatusCode403 () throws Exception {
        doNothing().when(customerRepository).deleteById(1L);

        mockMvc.perform(delete("http://localhost:8080/v1/admin/customers/{id}", 1))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenSaveHasRoleAdminAndCustomerIsNull_thenReturnStatusCode404 () throws Exception {

        Customer customer = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucas@email.com")
                .phoneNumber("11948729034")
                .build();

        when(customerRepository.save(customer)).thenReturn(customer);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customer);


        mockMvc.perform(post("http://localhost:8080/v1/admin/customers/").contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "xx", password = "xx", roles = "USER")
    public void whenListAllCustomersUsingCorrectRole_thenReturnCorrectData () throws Exception {
        List<Customer> customers = asList(new Customer
                        .Builder()
                        .name("Lucas1")
                        .documentNumber("123456789021")
                        .dateOfBirth("06-03-1993")
                        .email("lucaskano1@email.com")
                        .phoneNumber("11948729034")
                        .build(),
                new Customer
                        .Builder()
                        .name("Lucas2")
                        .documentNumber("123456789031")
                        .dateOfBirth("06-03-1990")
                        .email("lucaskano2@email.com")
                        .phoneNumber("11948729030")
                        .build());

        Page<Customer> pagedCustomers = new PageImpl(customers);

        when(customerRepository.findAll(isA(Pageable.class))).thenReturn(pagedCustomers);

        mockMvc.perform(get("http://localhost:8080/v1/admin/"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Lucas1"))
                .andExpect(jsonPath("$.content[0].documentNumber").value("123456789021"))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value("06-03-1993"))
                .andExpect(jsonPath("$.content[0].email").value("lucaskano1@email.com"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("11948729034"))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Lucas2"))
                .andExpect(jsonPath("$.content[0].documentNumber").value("123456789031"))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value("06-03-1990"))
                .andExpect(jsonPath("$.content[0].email").value("lucaskano2@email.com"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("11948729030"));

        verify(customerRepository).findAll(isA(Pageable.class));
    }


}
