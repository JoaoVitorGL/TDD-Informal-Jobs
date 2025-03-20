package org.featherlessbipeds.gouveia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Freelancer {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String cpf;
    private List<FreelancerTagsEnum> tags;
}
