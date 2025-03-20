package org.featherlessbipeds.gouveia.repository;

import org.featherlessbipeds.gouveia.model.Freelancer;

import java.util.ArrayList;
import java.util.List;

public class FreelancerRepository {
    private final List<Freelancer> freelancers = new ArrayList<>();

    public boolean existsByEmail(String email) {
        return freelancers.stream().anyMatch(freelancer -> freelancer.getEmail().equals(email));
    }

    public boolean existsByCpf(String cpf) {
        return freelancers.stream().anyMatch(freelancer -> freelancer.getCpf().equals(cpf));
    }

    public void save(Freelancer freelancer) {
        freelancers.add(freelancer);
    }
}
