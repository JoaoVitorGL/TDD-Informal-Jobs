package org.featherlessbipeds.service.contracts;

import org.featherlessbipeds.model.LocationEntity;

public interface LocationService {

     // Verifica se a localizacao nao esta apontando para o meio do oceano
     boolean isValidLocation(LocationEntity location);

     //Verifica se a localizacao é muito distande do CEP do usuario
     boolean isLocationFarFromAddress(LocationEntity location, String CEP);

}
