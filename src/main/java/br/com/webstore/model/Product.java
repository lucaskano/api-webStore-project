package br.com.webstore.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Project: api-webstore
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 22/04/2020
 */

@Entity
@Table(name = "tb_products")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Product name is required")
    private String name;

    @NotEmpty(message = "Product description is required")
    @Column(nullable = false)
    private String description;

    @NotEmpty(message = "Product price is required")
    private double price;

    @NotEmpty(message = "Product weight is required")
    private double weight;

    @NotEmpty(message = "Product quantity is required")
    private int quantity;

    private String pictureURL;

    public Product(){

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }


    public static final class Builder {
        private String name;
        private String description;
        private double price;
        private double weight;
        private int quantity;
        private String pictureURL;

        private Builder() {
        }

        public static Builder aProduct() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder pictureURL(String pictureURL) {
            this.pictureURL = pictureURL;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setWeight(weight);
            product.setQuantity(quantity);
            product.setPictureURL(pictureURL);
            return product;
        }
    }
}
