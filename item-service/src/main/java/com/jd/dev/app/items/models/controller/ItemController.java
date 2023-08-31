package com.jd.dev.app.items.models.controller;

import com.jd.dev.app.commons.models.entity.Product;
import com.jd.dev.app.items.models.Item;
import com.jd.dev.app.items.models.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
@RequestMapping()
public class ItemController {
    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Environment environment;

    @Value("${config.text}")
    String text;

    public ItemController(@Qualifier("feignService") ItemService itemService, CircuitBreakerFactory circuitBreakerFactory, Environment environment) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.environment = environment;
    }

    @GetMapping("")
    public ResponseEntity<List<Item>> findAll(@RequestParam(name = "name") String name, @RequestHeader(name = "token") String token) {
        System.out.println(name);
        System.out.println(token);
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}/{qty}")
    public ResponseEntity<Item> findById(@PathVariable Long id, @PathVariable Integer qty) {
        return circuitBreakerFactory.create("items")
                .run(() -> ResponseEntity.ok(itemService.findById(id, qty)), error -> alternativeMethod(id, qty));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "alternativeMethod")
    @GetMapping("/find/{id}/{qty}")
    public ResponseEntity<Item> findById2(@PathVariable Long id, @PathVariable Integer qty) {
        return ResponseEntity.ok(itemService.findById(id, qty));
    }

    /**
     * En caso de querer combinar ambas anotaciones, el fallbackMethod solo debe estar en @CircuitBreaker
     */
    @CircuitBreaker(name = "items", fallbackMethod = "alternativeMethod")
    @TimeLimiter(name = "items")
    @GetMapping("/find3/{id}/{qty}")
    public CompletableFuture<ResponseEntity<Item>> findById3(@PathVariable Long id, @PathVariable Integer qty) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(itemService.findById(id, qty)));
    }

    public ResponseEntity<Item> alternativeMethod(Long id, Integer qty) {
        Item item = new Item();
        item.setQty(qty);

        Product product = new Product();
        product.setId(id);
        product.setName("Oster Primal Latte");
        product.setPrice(500.00);

        item.setProduct(product);

        return ResponseEntity.ok(item);
    }
    public CompletableFuture<ResponseEntity<Item>> alternativeMethod2(Long id, Integer qty) {
        Item item = new Item();
        item.setQty(qty);

        Product product = new Product();
        product.setId(id);
        product.setName("Oster Primal Latte");
        product.setPrice(500.00);

        item.setProduct(product);

        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(item));
    }

    @GetMapping("/get-config")
    public ResponseEntity<?> getConfig(@Value("${server.port}") String port) {
        Map<String, String> properties = new HashMap<>();
        properties.put("text", text);
        properties.put("port", port);

        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev")) {
            properties.put("author.name", environment.getProperty("config.author.name"));
            properties.put("author.email", environment.getProperty("config.author.email"));
        }

        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
}
