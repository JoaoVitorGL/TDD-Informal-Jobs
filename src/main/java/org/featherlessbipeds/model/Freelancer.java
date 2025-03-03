package org.featherlessbipeds.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Freelancer  extends User
{
    private Integer id;
    private String name;
    private String surname;
    private String cep;
    private String photo;

}
