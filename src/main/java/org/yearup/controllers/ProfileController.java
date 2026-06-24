package org.yearup.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

@RestController
@RequestMapping("/profile")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
public class ProfileController {
    private ProfileService profileService;
    private UserService userService;

    // Get - http://localhost:8080/profile - No Body
    @GetMapping

    //Put - http://localhost:8080/profile - Profile Body
    @PutMapping

}
