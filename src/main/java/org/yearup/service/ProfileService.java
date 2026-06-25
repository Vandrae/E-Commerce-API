package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    public Profile getProfileById(int userId) {
        return profileRepository.findById(userId).orElse(null);
    }

    public Profile update(int userId, Profile profile){
        profile.setUserId(userId);
        profileRepository.save(profile);
        return getProfileById(userId);
    }
}
