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
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // Get - http://localhost:8080/profile - No Body
    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        Profile profile = profileService.getProfileById(userId);
        return ResponseEntity.ok(profile);
    }


    //Put - http://localhost:8080/profile - Profile Body
    @PutMapping
    public Profile editProfile(Principal principal, @RequestBody Profile profile){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        return profileService.update(userId,profile);
    }


}
