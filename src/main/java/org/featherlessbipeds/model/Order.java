package org.featherlessbipeds.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.featherlessbipeds.Enums.ServicesCategoriesEnum;

@Builder
@Getter
@Setter
public class Order {
    private Integer id;
    private ServicesCategoriesEnum serviceCategory;
    private Integer termInDays;
    private String description;
    private Double budget;
    private String attachment;
    private LocationEntity location;
    private Client client;
    private Freelancer freelancer;

}
