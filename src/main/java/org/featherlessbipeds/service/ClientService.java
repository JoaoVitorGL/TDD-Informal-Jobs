package org.featherlessbipeds.service;

import org.featherlessbipeds.model.Client;
import org.featherlessbipeds.repository.contracts.ClientRepository;
import org.featherlessbipeds.exception.EditProfileException;
import org.featherlessbipeds.utils.EditProfileFlag;

import java.util.Optional;

public class ClientService
{
    private final ClientRepository repository;

    public ClientService(ClientRepository repository)
    {
        this.repository = repository;
    }

    public Client editProfile(Client originalClient, Client newClientData) throws EditProfileException
    {
        if (newClientData.getName().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_NAME);
        if (newClientData.getSurname().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_SURNAME);
        if (newClientData.getEmail().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_EMAIL);
        if (newClientData.getCep().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_CEP);
        if (!isValidEmail(newClientData.getEmail()))
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_INVALID_EMAIL);

        Client existingClient = repository.findById(originalClient.getId())
                .orElseThrow(() -> new EditProfileException(EditProfileFlag.EDIT_PROFILE_ERROR));

        // First time I actually saw a nested if being useful.
        if (!newClientData.getEmail().equals(existingClient.getEmail()))
        {
            Optional<Client> op = repository.findByEmail(newClientData.getEmail());

            // If the email exists and is not from the current user being edited.
            if (op.isPresent() && !op.get().getId().equals(existingClient.getId()))
                throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMAIL_CONFLICT);
        }

        return repository.update(newClientData)
                .orElseThrow(() -> new EditProfileException(EditProfileFlag.EDIT_PROFILE_ERROR));
    }

    private boolean isValidEmail(String email)
    {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}