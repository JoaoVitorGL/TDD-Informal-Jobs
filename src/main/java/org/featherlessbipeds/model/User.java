package org.featherlessbipeds.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User
{
    protected String email;
    protected String password;
}
