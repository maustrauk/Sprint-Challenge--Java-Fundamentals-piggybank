package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CoinsController {
    @Autowired
    private CoinsRepository coinsRepository;

    //http://localhost:2019/total
    @GetMapping(value = "/total", produces = "application/json")
    public ResponseEntity<?> getMoney() {
        List<Coin> myList = new ArrayList<>();
        coinsRepository.findAll().iterator().forEachRemaining(myList::add);

        int quantity;
        String formattedName;
        double sum=0;

        for (Coin coin : myList) {
            quantity = coin.getQuantity();

            sum += quantity * coin.getValue();

            if (quantity == 1) {
                formattedName = coin.getName();
            }else {
                formattedName = coin.getNameplural();
            }

            System.out.println( quantity + " " + formattedName);
        }

        System.out.println("The piggy bank holds " + sum);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
