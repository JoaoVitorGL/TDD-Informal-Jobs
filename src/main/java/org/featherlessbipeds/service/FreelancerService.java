package org.featherlessbipeds.service;

import org.featherlessbipeds.exception.EditProfileException;
import org.featherlessbipeds.model.Freelancer;
import org.featherlessbipeds.repository.contracts.FreelancerRepository;
import org.featherlessbipeds.utils.EditProfileFlag;



public class FreelancerService {

    private  FreelancerRepository repository;

    public FreelancerService (FreelancerRepository repository){this.repository = repository;}


    public Freelancer updateFreelancer(Freelancer originalData , Freelancer newData ) throws EditProfileException {

        if (newData.getName().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_NAME);
        if (newData.getSurname().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_SURNAME);
        if (newData.getEmail().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_EMAIL);
        if (newData.getCep().isEmpty())
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMPTY_CEP);
        if (!isValidEmail(newData.getEmail()))
            throw new EditProfileException(EditProfileFlag.EDIT_PROFILE_INVALID_EMAIL);

       Freelancer existingFreelancer = repository.findById(originalData.getId()).orElseThrow(() -> new EditProfileException(EditProfileFlag.EDIT_PROFILE_ERROR));

       Freelancer checkEmail = repository.findByEmail(newData.getEmail()).orElse(null);

       if(checkEmail == null || checkEmail.getId().equals(originalData.getId())){

           existingFreelancer.setName(newData.getName());
           existingFreelancer.setSurname(newData.getSurname());
           existingFreelancer.setEmail(newData.getEmail());
           existingFreelancer.setCep(newData.getCep());
           existingFreelancer.setPhoto(newData.getPhoto());

           return repository.update(existingFreelancer).orElseThrow(() -> new EditProfileException(EditProfileFlag.EDIT_PROFILE_ERROR));

       }

       throw  new EditProfileException(EditProfileFlag.EDIT_PROFILE_EMAIL_CONFLICT);

    }

    private boolean isValidEmail(String email)
    {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
