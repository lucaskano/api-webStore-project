package br.com.webstore;

import br.com.webstore.Repository.CustomerRepository;
import br.com.webstore.Repository.DBUserRepository;
import br.com.webstore.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;
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
public class CustomerEndpointTokenTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private DBUserRepository userRepository;

    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Customer> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @BeforeEach
    public void configProtectedHeaders() {

        String str = "{\"username\": \"kanoteste\", \"password\": \"lucaskano\"}";

        HttpHeaders headers = restTemplate.postForEntity("http://localhost:8080/login", str, String.class).getHeaders();

        this.protectedHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void configAdminHeaders() {
        String str = "{\"username\": \"testeDoTeste\", \"password\": \"lucaskano2\"}";
        HttpHeaders headers = restTemplate.postForEntity("http://localhost:8080/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void configWrongHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "11111");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void setup() {
        Customer customer = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucaskano@email.com")
                .phoneNumber("11948729034")
                .build();

        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
    }


    @Test
    public void whenListCustomerUsingIncorrectToken_thenReturnStatusCode403Forbidden () {

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/", GET, wrongHeader, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenGetCustomerByIdUsingIncorrectToken_thenReturnStatusCode403Forbidden () {

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/1", GET, wrongHeader, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenListCustomerUsingCorrectToken_thenReturnStatusCode200OK () {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/", GET, protectedHeader, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void whenFindCustomerByNameUsingIncorrectToken_thenReturnStatusCode403Forbidden () {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/findByName/kanoteste", GET, wrongHeader, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenGetCustomerByIdUsingCorrectTokenAndStudentDontExist_thenReturnStatusCode404NotFound () {

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/-1", GET, protectedHeader, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void whenSaveCustomerUsingIncorrectToken_thenReturnStatusCode403Forbidden() {
        Customer customer = new Customer
                .Builder()
                .name("Lucas Um")
                .documentNumber("123456789021")
                .dateOfBirth("06-03-1993")
                .email("lucaskano1@email.com")
                .phoneNumber("11948729034")
                .build();

        adminHeader = new HttpEntity<>(customer, adminHeader.getHeaders());


        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/protected/customers/", POST, wrongHeader, String.class);


        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenSaveStudentUsingCorrectToken_thenReturn201Created () {
        Customer student = new Customer
                .Builder()
                .name("Lucas Dois")
                .documentNumber("123456789022")
                .dateOfBirth("06-03-1999")
                .email("lucaskano2@email.com")
                .phoneNumber("11948729032")
                .build();

        adminHeader = new HttpEntity<>(student, adminHeader.getHeaders());

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/protected/customers/", POST, adminHeader, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void whenDeleteCustomerUsingIncorrectToken_thenReturnStatusCode403Forbidden () {

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/{id}", DELETE, wrongHeader, String.class, 1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenDeleteCustomerUsingCorrectToken_thenReturnStatusCode200Ok () throws JSONException {
        Customer customer = new Customer
                .Builder()
                .name("Lucas Tres")
                .documentNumber("123456789023")
                .dateOfBirth("06-03-1991")
                .email("lucaskano3@email.com")
                .phoneNumber("11948729031")
                .build();

        adminHeader = new HttpEntity<>(customer, adminHeader.getHeaders());

        ResponseEntity<String> response1 = restTemplate.exchange("http://localhost:8080/v1/admin/customers/", POST, adminHeader, String.class);

        JSONObject studentJson = new JSONObject(response1.getBody());

        doNothing().when(customerRepository).deleteById(studentJson.getLong("id"));

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/{id}", DELETE, adminHeader, String.class, studentJson.getLong("id"));

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void whenUpdateCustomerUsingCorrectToken_thenReturnStatusCode200Ok () throws JSONException {

        Customer customer = new Customer
                .Builder()
                .name("Lucas Quatro")
                .documentNumber("123456789026")
                .dateOfBirth("06-03-1998")
                .email("lucaskano4@email.com")
                .phoneNumber("11948729031")
                .build();

        adminHeader = new HttpEntity<>(customer, adminHeader.getHeaders());

        ResponseEntity<String> response1 = restTemplate.exchange("http://localhost:8080/v1/admin/customers/", POST, adminHeader, String.class);

        JSONObject customerJson = new JSONObject(response1.getBody());

        doNothing().when(customerRepository).deleteById(customerJson.getLong("id"));

        Customer customer1 = new Customer
                .Builder()
                .name("Lucas Quatro")
                .documentNumber("123456789026")
                .dateOfBirth("06-03-1998")
                .email("lucaskano4@email.com")
                .phoneNumber("11948729031")
                .build();

        adminHeader = new HttpEntity<>(customer1, adminHeader.getHeaders());

        restTemplate.exchange("http://localhost:8080/v1/admin/customers/", POST, adminHeader, String.class);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/", PUT, adminHeader, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void whenUpdateCustomerUsingIncorrectToken_thenReturnStatusCode403Forbidden () {


        Customer customer = new Customer
                .Builder()
                .name("Lucas Quatro")
                .documentNumber("123456789026")
                .dateOfBirth("06-03-1998")
                .email("lucaskano4@email.com")
                .phoneNumber("11948729031")
                .build();

        customerRepository.save(customer);

        adminHeader = new HttpEntity<>(customer, adminHeader.getHeaders());

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/v1/admin/customers/", PUT, wrongHeader, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }


    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenListAllCustomersUsingCorrectRole_thenReturnStatusCode200 () throws Exception {
        List<Customer> customers = asList(new Customer
                        .Builder()
                        .name("Lucas Cinco")
                        .documentNumber("123456769026")
                        .dateOfBirth("06-03-1994")
                        .email("lucaskano5@email.com")
                        .phoneNumber("11948729031")
                        .build(),
                new Customer
                        .Builder()
                        .name("Lucas Seis")
                        .documentNumber("123456789023")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano6@email.com")
                        .phoneNumber("11948729032")
                        .build());

        Page<Customer> pagedCustomers = new PageImpl(customers);

        when(customerRepository.findAll(isA(Pageable.class))).thenReturn(pagedCustomers);

        mockMvc.perform(get("http://localhost:8080/v1/admin/customers/"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(customerRepository).findAll(isA(Pageable.class));
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenGetCustomerByIdUsingCorrectRoleAndStudentDoesntExist_thenReturnStatusCode404 () throws Exception {
        Customer customer = new Customer
                .Builder()
                .name("Lucas Sete")
                .documentNumber("123456389021")
                .dateOfBirth("06-03-1998")
                .email("lucaskano7@email.com")
                .phoneNumber("11948729032")
                .build();

        when(customerRepository.findById(3L)).thenReturn(java.util.Optional.of(customer));

        mockMvc.perform(get("http://localhost:8080/v1/admin/students/{id}", 6))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(customerRepository).findById(6L);
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenFindCustomersByNameUsingCorrectRole_thenReturnStatusCode200 () throws Exception {
        List<Customer> students = asList(new Customer
                        .Builder()
                        .name("Lucas Oito")
                        .documentNumber("123456789023")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano8@email.com")
                        .phoneNumber("11948729032")
                        .build(),
                new Customer
                        .Builder()
                        .name("Lucas Nove")
                        .documentNumber("123456789023")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano9@email.com")
                        .phoneNumber("11948729032")
                        .build(),
                new Customer
                        .Builder()
                        .name("Lucas Dez")
                        .documentNumber("123456789023")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano10@email.com")
                        .phoneNumber("11948729032")
                        .build());

        when(customerRepository.findByNameIgnoreCaseContaining("lucas")).thenReturn(students);

        mockMvc.perform(get("http://localhost:8080/v1/admin/customers/findByName/legolas"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(customerRepository).findByNameIgnoreCaseContaining("legolas");
    }

    @Test
    @WithMockUser(username = "xxx", password = "xxx", roles = {"ADMIN"})
    public void whenDeleteUsingCorrectRole_thenReturnStatusCode200 () throws Exception {

        Customer customer =  new Customer
                .Builder()
                .name("Lucas Onze")
                .documentNumber("123456789023")
                .dateOfBirth("06-03-1998")
                .email("lucaskano11@email.com")
                .phoneNumber("11948729032")
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
                .name("Lucas Doze")
                .documentNumber("123456789023")
                .dateOfBirth("06-03-1998")
                .email("lucaskano12@email.com")
                .phoneNumber("11948729032")
                .build();

        when(customerRepository.save(customer)).thenReturn(customer);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customer);


        mockMvc.perform(post("http://localhost:8080/v1/admin/students/").contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "xx", password = "xx", roles = "USER")
    public void whenListAllStudentsUsingCorrectRole_thenReturnCorrectData () throws Exception {
        List<Customer> customers = asList(new Customer
                        .Builder()
                        .name("Lucas Treze")
                        .documentNumber("123456789023")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano13@email.com")
                        .phoneNumber("11948729032")
                        .build(),
                new Customer
                        .Builder()
                        .name("Lucas Zero")
                        .documentNumber("123456789023")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano0@email.com")
                        .phoneNumber("11948729032")
                        .build());

        Page<Customer> pagedCustomers = new PageImpl(customers);

        when(customerRepository.findAll(isA(Pageable.class))).thenReturn(pagedCustomers);

        mockMvc.perform(get("http://localhost:8080/v1/protected/students/"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Lucas Treze"))
                .andExpect(jsonPath("$.content[0].documentNumber").value("123456789023"))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value("06-03-1998"))
                .andExpect(jsonPath("$.content[0].email").value("lucaskano13@email.com"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("11948729032"))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].name").value("Lucas Zero"))
                .andExpect(jsonPath("$.content[0].documentNumber").value("123456789023"))
                .andExpect(jsonPath("$.content[0].dateOfBirth").value("06-03-1998"))
                .andExpect(jsonPath("$.content[0].email").value("lucaskano0@email.com"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("11948729032"));

        verify(customerRepository).findAll(isA(Pageable.class));
    }

}