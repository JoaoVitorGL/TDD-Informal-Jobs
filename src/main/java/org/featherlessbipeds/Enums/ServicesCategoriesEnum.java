package org.featherlessbipeds.Enums;

public enum ServicesCategoriesEnum {
    SERVICOS_DOMESTICOS("Serviços Domésticos"),
    ASSISTENCIA_TECNICA("Assistência Técnica"),
    REFORMA_E_CONSTRUCAO("Reforma e Construção"),
    AUTOMOVEIS("Automóveis"),
    SAUDE("Saúde"),
    AULAS("Aulas"),
    PET("Pet"),
    CONSULTORIA("Consultoria"),
    TECNOLOGIA("Tecnologia"),
    EVENTOS("Eventos"),
    MODA("Moda"),
    BELEZA("Beleza");

    private final String descricao;

    ServicesCategoriesEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

