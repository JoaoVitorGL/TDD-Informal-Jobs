package org.featherlessbipeds.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User
{
    private Integer id;
    private String name;
    private String surname;
    private String cep;
    private String photo;
}
