package org.featherlessbipeds.gouveia.service;

import org.featherlessbipeds.gouveia.exception.RegisterException;
import org.featherlessbipeds.gouveia.model.Freelancer;
import org.featherlessbipeds.gouveia.repository.FreelancerRepository;

import java.util.regex.Pattern;

public class FreelancerRegisterService {
    private FreelancerRepository freelancerRepository;
    private static final String code = "123456";

    public FreelancerRegisterService(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    public Freelancer registerFreelancer(Freelancer freelancer, String confirmPassword, String confirmationCode) throws RegisterException {
        validateFields(freelancer, confirmPassword, confirmationCode);

        if (freelancerRepository.existsByEmail(freelancer.getEmail())) {
            throw new RegisterException("Email already registered.");
        } else if (freelancerRepository.existsByCpf(freelancer.getCpf())) {
            throw new RegisterException("CPF already registered.");
        }
        freelancerRepository.save(freelancer);
        return freelancer;
    }

    public void validateFields(Freelancer freelancer, String confirmPassword, String confirmationCode) throws RegisterException {

        if (freelancer.getName() == null || freelancer.getName().trim().isEmpty()) {
            throw new RegisterException("The name field cannot be blank.");
        }

        if (freelancer.getSurname() == null || freelancer.getSurname().trim().isEmpty()) {
            throw new RegisterException("The surname field cannot be blank.");
        }

        if (freelancer.getEmail() == null || freelancer.getEmail().trim().isEmpty()) {
            throw new RegisterException("The e-mail field cannot be blank.");
        }

        if (freelancer.getCpf() == null || freelancer.getCpf().trim().isEmpty()) {
            throw new RegisterException("The CPF field cannot be blank.");
        }

        if (freelancer.getPassword() == null || freelancer.getPassword().trim().isEmpty()) {
            throw new RegisterException("The password field cannot be blank.");
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new RegisterException("The confirm password field cannot be blank.");
        }

        if (freelancer.getTags() == null || freelancer.getTags().isEmpty()) {
            throw new RegisterException("Select at least one tag.");
        }

        if (!Pattern.matches("[a-zA-Z\\s]+", freelancer.getName())) {
            throw new RegisterException("Invalid name. It must only contain letters and spaces.");
        }

        if (!Pattern.matches("[a-zA-Z\\s]+", freelancer.getSurname())) {
            throw new RegisterException("Invalid surname. It must only contain letters and spaces.");
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", freelancer.getEmail())) {
            throw new RegisterException("Invalid e-mail.");
        }

        if (!Pattern.matches("^\\d{11}$", freelancer.getCpf())) {
            throw new RegisterException("Invalid CPF.");
        }

        if (!Pattern.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$", freelancer.getPassword())) {
            throw new RegisterException("Invalid password. It must have at least 8 characters, an uppercase letter, and a number.");
        }

        if (!freelancer.getPassword().equals(confirmPassword)) {
            throw new RegisterException("Password confirmation does not match.");
        }

        if (!confirmationCode.equals(code)) {
            throw new RegisterException("Invalid confirmation code.");
        }
    }
}
