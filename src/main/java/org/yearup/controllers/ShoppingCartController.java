package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart controller depends on the service layer
    private ShoppingCartService shoppingCartService;
    private UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService){
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        return shoppingCartService.getByUserId(userId);
    }

    @PostMapping("/products/{id}")
    public ResponseEntity<ShoppingCart> addToCart(Principal principal, @PathVariable("id") int productId){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        ShoppingCart saved = shoppingCartService.addToCart(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15  (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
    @PutMapping("/{id}")
    public ShoppingCartItem editCart(Principal principal, @PathVariable("id") int productId,
                                     @RequestBody ShoppingCartItem shoppingCartItem){
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();
        int quantity = shoppingCartItem.getQuantity();
        if (shoppingCartService.getByUserId(userId) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return shoppingCartService.update(principal, productId, quantity);
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)

}
