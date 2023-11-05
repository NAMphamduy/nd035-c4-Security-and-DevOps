package com.example.demo.controller;
import java.math.BigDecimal;import java.util.Optional;
import org.junit.Before;import org.junit.Test;import org.springframework.http.HttpStatus;
import com.example.demo.model.persistence.Item;import org.springframework.http.ResponseEntity;
import com.example.demo.model.persistence.User;import com.example.demo.model.persistence.repositories.UserRepository;import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.helpers.Utils;import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;import static org.junit.Assert.assertNotNull;
import com.example.demo.model.persistence.repositories.ItemRepository;import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;import static org.mockito.Mockito.mock;
public class CartControlerTest {
    private CartController cartController;
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        Utils.injectObjects(cartController, "cartRepository", cartRepository);
        Utils.injectObjects(cartController, "userRepository", userRepository);
        Utils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCartHappyPath(){
        User user = new User();
        user.setUsername("username1");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("username1");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Cart cart = user.getCart();
        cart.addItem(item);
        when(cartRepository.save(cart)).thenReturn(cart);
        ResponseEntity<Cart> response = ResponseEntity.ok(cart);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addToCartSadPath(){
        ResponseEntity<Cart> response = cartController.addToCart(new ModifyCartRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void removeFromCartHappyPath(){
        User user = new User();
        user.setUsername("username1");
        user.setCart(new Cart());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("username1");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Cart cart = user.getCart();
        cart.removeItem(item);
        when(cartRepository.save(cart)).thenReturn(cart);
        ResponseEntity<Cart> response = ResponseEntity.ok(cart);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void removeFromCartSadPath(){
        ResponseEntity<Cart> response = cartController.removeFromCart(new ModifyCartRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}