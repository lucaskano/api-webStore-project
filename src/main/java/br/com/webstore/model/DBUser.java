package br.com.webstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Project: api-webstore
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 22/04/2020
 */
@Entity
@Table(name = "tb_users")
public class DBUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private boolean admin;

    public DBUser(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public static final class Builder {
        private String name;
        private String username;
        private String password;
        private boolean admin;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder admin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public DBUser build() {
            DBUser dBUser = new DBUser();
            dBUser.setName(name);
            dBUser.setUsername(username);
            dBUser.setPassword(password);
            dBUser.setAdmin(admin);
            return dBUser;
        }
    }
}
