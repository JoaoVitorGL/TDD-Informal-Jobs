package org.featherlessbipeds.model;

import lombok.Builder;
import org.featherlessbipeds.Enums.ServicesCategoriesEnum;

@Builder
public class Order {

    private ServicesCategoriesEnum serviceCategory;
    private Integer termInDays;
    private String description;
    private Double budget;
    private String attachment;
}
