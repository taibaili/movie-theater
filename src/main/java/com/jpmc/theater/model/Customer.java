package com.jpmc.theater.model;

import lombok.Data;

@Data
public class Customer {

    private String name;

    private String id;

    /**
     * @param name customer name
     * @param id   customer id
     */
    public Customer(String name, String id) {
        this.id = id; // NOTE - id is not used anywhere at the moment
        this.name = name;
    }
}