package br.com.webstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Project: api-webstore
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 22/04/2020
 */
@Entity
@Table(name = "tb_customers")
public class Customer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The Customer name is required")
    private String name;

    @NotEmpty(message = "The Customer documentNumber is required")
    private String documentNumber;

    @NotEmpty(message = "The Customer address is required")
    private String address;

    @Embedded
    //@NotEmpty(message = "The State field of Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    private State state;

    @Embedded
    //@NotEmpty(message = "The City field of Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    @Embedded
    //@NotEmpty(message = "The Country field of Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotEmpty(message = "The Customer dateOfBirth is required")
    private String dateOfBirth;

    @NotEmpty(message = "The Customer phoneNumber is required")
    private String phoneNumber;

    @NotEmpty(message = "The Customer email is required")
    @Email(message = "The email field must be valid")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static final class Builder {
        private String name;
        private String documentNumber;
        private String address;
        private State state;
        private City city;
        private String dateOfBirth;
        private String phoneNumber;
        private String email;

        public Builder() {
        }

        public static Builder aCustomer() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder documentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder state(State state) {
            this.state = state;
            return this;
        }

        public Builder city(City city) {
            this.city = city;
            return this;
        }

        public Builder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setName(name);
            customer.setDocumentNumber(documentNumber);
            customer.setAddress(address);
            customer.setState(state);
            customer.setCity(city);
            customer.setDateOfBirth(dateOfBirth);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
            return customer;
        }
    }
}
