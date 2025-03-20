package org.featherlessbipeds.gouveia.repository;

import org.featherlessbipeds.gouveia.entity.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository
{
    private final List<Client> clients = new ArrayList<>();
    public boolean existsByEmail(String email) {
        return clients.stream().anyMatch(client -> client.getEmail().equals(email));
    }
    public void save(Client client) {
        clients.add(client);
    }
}
