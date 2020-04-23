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
@Table(name = "tb_state")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The State name is required")
    @Column(nullable = false)
    private String name;

    @NotEmpty
    private String uf;

    @Embedded
    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    public State(){

    }

    public State(@NotEmpty(message = "The State name is required") String name, @NotEmpty String uf, Country country) {
        this.name = name;
        this.uf = uf;
        this.country = country;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }


}
