package org.featherlessbipeds.repository.contracts;

import org.featherlessbipeds.model.Client;

import java.util.Optional;

public interface FreelancerRepository {

    Optional<Client> findById(Integer id);
    Optional<Client> findByEmail(String email);
    Optional<Client> update(Client newClientData);

}
