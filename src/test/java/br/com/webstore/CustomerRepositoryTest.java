package br.com.webstore;

import br.com.webstore.Repository.CustomerRepository;
import br.com.webstore.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project: api-webstore
 *
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 23/04/2020
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // -> tests using the database itself
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenCreate_thenPersistData () {

        Customer customer = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucaskano@email.com")
                .phoneNumber("11948729034")
                .build();
        this.customerRepository.save(customer);

        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getName()).isEqualTo("Lucas");
        assertThat(customer.getDocumentNumber()).isEqualTo("123456789011");
        assertThat(customer.getDateOfBirth()).isEqualTo("06-03-1998");
        assertThat(customer.getEmail()).isEqualTo("lucaskano@email.com");
        assertThat(customer.getPhoneNumber()).isEqualTo("11948729034");

    }

    @Test
    public void whenDelete_thenRemoveData () {
        Customer customer = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucaskano@email.com")
                .phoneNumber("11948729034")
                .build();
        this.customerRepository.save(customer);
        customerRepository.delete(customer);
        assertThat(customerRepository.findById(customer.getId())).isEmpty();

    }

    @Test
    public void whenUpdate_thenChangeAndPersistData () {
        Customer customer = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucaskano@email.com")
                .phoneNumber("11948729034")
                .build();
        this.customerRepository.save(customer);

        customer = new Customer
                .Builder()
                .name("Lucas Dois")
                .documentNumber("123456789012")
                .dateOfBirth("06-03-1997")
                .email("lucaskano2@email.com")
                .phoneNumber("11948729033")
                .build();
        this.customerRepository.save(customer);

        customer = customerRepository.findById(customer.getId()).orElse(null);

        assertThat(customer.getName()).isEqualTo("Marcos Dois");
        assertThat(customer.getEmail()).isEqualTo("marcos.testedois@email.com");
    }

    @Test
    public void whenFindByNameIgnoreCaseContaining_thenIgnoreCase () {
        Customer customer1 = new Customer
                .Builder()
                .name("Lucas")
                .documentNumber("123456789011")
                .dateOfBirth("06-03-1998")
                .email("lucaskano@email.com")
                .phoneNumber("11948729034")
                .build();
        Customer customer2 = new Customer
                .Builder()
                .name("Lucas Dois")
                .documentNumber("123456789012")
                .dateOfBirth("06-03-1997")
                .email("lucaskano2@email.com")
                .phoneNumber("11948729033")
                .build();

        this.customerRepository.save(customer1);
        this.customerRepository.save(customer2);

        List<Customer> studentList = customerRepository.findByNameIgnoreCaseContaining("lucas");

        assertThat(studentList.size()).isEqualTo(2);

    }

    @Test
    public void whenNotEmptyName_thenNoConstraintViolations () {
        Exception exception = assertThrows(
                ConstraintViolationException.class,
                () -> customerRepository.save(new Customer
                        .Builder()
                        .name("Lucas")
                        .documentNumber("123456789011")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano@email.com")
                        .phoneNumber("11948729033")
                        .build()));

        assertTrue(exception.getMessage().contains("The Customer name is required"));
    }

    @Test
    public void whenNotEmptyEmail_thenNoConstraintViolations () {
        Exception exception = assertThrows(
                ConstraintViolationException.class,
                () -> customerRepository.save(new Customer
                        .Builder()
                        .name("Lucas")
                        .documentNumber("123456789011")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano@email.com")
                        .phoneNumber("11948729033")
                        .build()));

        assertTrue(exception.getMessage().contains("The Customer email is required"));
    }

    @Test
    public void whenValidEmail_thenNoConstraintViolations () {
        Exception exception = assertThrows(
                ConstraintViolationException.class,
                () -> customerRepository.save(new Customer
                        .Builder()
                        .name("Lucas")
                        .documentNumber("123456789011")
                        .dateOfBirth("06-03-1998")
                        .email("lucaskano@email.com")
                        .phoneNumber("11948729033")
                        .build()));

        assertTrue(exception.getMessage().contains("The email field must be valid"));
    }

}