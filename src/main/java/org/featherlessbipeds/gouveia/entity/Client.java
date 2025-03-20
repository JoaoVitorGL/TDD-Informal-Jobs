package org.featherlessbipeds.gouveia.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Client {

    private String name;
    private String surname;
    private String email;
    private String password;
}
