package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);
        ShoppingCart cart = new ShoppingCart();
        for(CartItem cartItem:cartItems){
            Product product = productService.getById(cartItem.getProductId());
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            cart.add(item);
        }
        return cart;
    }


    public ShoppingCart addToCart(int userId, int productId){
    CartItem items = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
    /* if there is nothing in the cart its getting the product id of what your wanting to add and adding it to your cart
      by getting your user id and setting the quantity of that product to 1
        */
    if (items == null){

        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setUserId(userId);
        cartItem.setQuantity(1);
        shoppingCartRepository.save(cartItem);

        /* if there is something in the cart matching the product id each time the method executes
         it increases the quantity by 1. if I knew it would work with the front end I would have them enter the amount
         they want to add instead of only adding one at a time
        */
    } else if (items != null) {
        items.setQuantity(items.getQuantity() + 1);
        shoppingCartRepository.save(items);
    }
        return getByUserId(userId);
    }

    // find the exact shopping cart with the item you want to update and set the quantity then save it to the correct cart
    public ShoppingCart update(int userId, int productId, int quantity){
        CartItem item = shoppingCartRepository.findByUserIdAndProductId(userId,productId);
        item.setQuantity(quantity);
        shoppingCartRepository.save(item);
        return getByUserId(userId);
    }

    //// find the exact shopping cart with the item you want to delete
    @Transactional
    public ShoppingCart delete(int userId){
        shoppingCartRepository.deleteByUserId(userId);
        return getByUserId(userId);
    }
}
