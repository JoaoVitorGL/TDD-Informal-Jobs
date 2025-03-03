package org.featherlessbipeds.repository.contracts;

import org.featherlessbipeds.model.Freelancer;

import java.util.Optional;

public interface FreelancerRepository {

    Optional<Freelancer> findById(Integer id);
    Optional<Freelancer> findByEmail(String email);
    Optional<Freelancer> update(Freelancer newFreelancer);

}
