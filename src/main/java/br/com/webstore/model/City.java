package br.com.webstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Project: api-webstore
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 23/04/2020
 */

@Embeddable
@Entity
@Table(name = "tb_city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The City name is required")
    @Column(nullable = false)
    private String name;

    @Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    private State state;

    public City(){

    }

    public City(@NotEmpty(message = "The City name is required") String name, State state) {
        this.name = name;
        this.state = state;
    }

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
