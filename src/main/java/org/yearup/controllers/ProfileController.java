package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.repository.UserRepository;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping("/profile")
//a user is required to be logged in to view their profile list all possible roles
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // able to view only your profile and all its fields
    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        Profile profile = profileService.getProfileById(userId);
        return ResponseEntity.ok(profile);
    }


    //abilty to edit fields on the profile
    @PutMapping
    public Profile editProfile(Principal principal, @RequestBody Profile profile){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        return profileService.update(userId,profile);
    }


}
