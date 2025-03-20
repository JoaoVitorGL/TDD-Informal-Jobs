package org.featherlessbipeds.gouveia.service;

import org.featherlessbipeds.gouveia.entity.Client;
import org.featherlessbipeds.gouveia.exception.RegisterException;
import org.featherlessbipeds.gouveia.repository.ClientRepository;

import java.util.regex.Pattern;

public class ClientRegisterService
{
    private ClientRepository clientRepository;
    private static final String code = "123456";
    public ClientRegisterService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(Client client, String confirmPassword, String confirmationCode) throws RegisterException {
        validateFields(client, confirmPassword, confirmationCode);

        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RegisterException("Email already registered.");
        }
        clientRepository.save(client);
        return client;
    }

    public void validateFields(Client client, String confirmPassword, String confirmationCode) throws RegisterException {

        if (client.getName() == null || !Pattern.matches("[a-zA-Z\\s]+", client.getName())) {
            throw new RegisterException("Invalid name. It must only contain letters and spaces.");
        }

        if (client.getSurname() == null || !Pattern.matches("[a-zA-Z\\s]+", client.getSurname())) {
            throw new RegisterException("Invalid surname. It must only contain letters and spaces.");
        }

        if (client.getEmail() == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", client.getEmail())) {
            throw new RegisterException("Invalid e-mail.");
        }

        if (client.getPassword() == null || !Pattern.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$", client.getPassword())) {
            throw new RegisterException("Invalid password. It must have at least 8 characters, an uppercase letter, and a number.");
        }

        if (!client.getPassword().equals(confirmPassword)) {
            throw new RegisterException("Password and confirm password do not match");
        }

        if (!confirmationCode.equals(code)) {
            throw new RegisterException("Invalid confirmation code.");
        }

        if (client.getName().trim().isEmpty()) {
            throw new RegisterException("The name field cannot be blank.");
        }

        if (client.getSurname().trim().isEmpty()) {
            throw new RegisterException("The surname field cannot be blank.");
        }

        if (client.getEmail().trim().isEmpty()) {
            throw new RegisterException("The e-mail field cannot be blank.");
        }

        if (client.getPassword().trim().isEmpty()) {
            throw new RegisterException("The password field cannot be blank.");
        }

        if (confirmPassword.trim().isEmpty()) {
            throw new RegisterException("The confirm password field cannot be blank.");
        }
    }
}
