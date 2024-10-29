package com.example.user_management_ms.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 9)
    @JsonProperty("cep")
    private String zipCode;

    @Column(length = 100)
    @JsonProperty("logradouro")
    private String street;

    @Column(length = 50)
    @JsonProperty("complemento")
    private String complement;

    @Column(length = 60)
    @JsonProperty("bairro")
    private String neighborhood;

    @Column(length = 100)
    @JsonProperty("localidade")
    private String city;

    @Column(length = 2)
    @JsonProperty("uf")
    private String state;
}
