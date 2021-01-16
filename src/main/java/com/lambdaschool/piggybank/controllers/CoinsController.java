package com.lambdaschool.piggybank.controllers;

import com.lambdaschool.piggybank.models.Coin;
import com.lambdaschool.piggybank.repositories.CoinsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class CoinsController {
    @Autowired
    private CoinsRepository coinsRepository;

    //http://localhost:2019/total
    @GetMapping(value = "/total", produces = "application/json")
    public ResponseEntity<?> total() {
        List<Coin> myList = new ArrayList<>();
        coinsRepository.findAll().iterator().forEachRemaining(myList::add);

        return getMoney(myList);
    }

    //http://localhost:2019/money/{amount}
    @GetMapping(value = "/money/{amount}", produces = "application/json")
    public ResponseEntity<?> remove(@PathVariable double amount) {
        List<Coin> myList = new ArrayList<>();
        coinsRepository.findAll().iterator().forEachRemaining(myList::add);

        return getMoney(updatedList(myList, amount));
    }

    private List<Coin> updatedList(List<Coin> myList, double amount) {

        List<Coin> updList = checkDuplicate(myList);
        double listSum = checkSum(updList);

        if (amount > 0 && amount <= listSum ) {
            return amountRemover(updList, amount);
        } else {
            System.out.println("Range exeсeed");
            return  new ArrayList<>();
        }
    }

    private List<Coin> amountRemover (List<Coin> list, double amount) {

        List<Coin> updList = new ArrayList<>();

        for (Coin coin : list) {
            Coin c = new Coin(coin.getName(), coin.getNameplural(), coin.getValue(), coin.getQuantity());
            updList.add(c);
        }

        double tempAmount = amount;

        int i = 0;

        for (Coin coin : list) {
            while ((tempAmount - coin.getPrise()) < 0 ) {
                coin.setQuantity(coin.getQuantity() - 1);
            }
            updList.get(i).setQuantity(updList.get(i).getQuantity() - coin.getQuantity());
            tempAmount = tempAmount - coin.getPrise();
            i++;
        }


        return  updList;
    }

    private List<Coin> checkDuplicate (List<Coin> list) {

        //sorted by value
        list.sort((c1, c2) -> (c1.getValue() > c2.getValue()) ? -1 : 1);

        List<Coin> updList = new ArrayList<Coin>();

        updList.add(list.get(0));

        int i = 1;

        while (i < list.size()) {
            Coin listCoin = list.get(i);
            int coinIndex = updList.size() - 1;
            Coin updCoin = updList.get(coinIndex);

            if(listCoin.getValue() != updCoin.getValue()) {
              updList.add(listCoin);
            } else {
                updCoin.setQuantity(updCoin.getQuantity() + listCoin.getQuantity());
            }

            i++;
        }


        return updList;



    }

    private double checkSum(List<Coin> list) {
        double sum = 0;
        for (Coin coin : list) {
            sum += coin.getQuantity() * coin.getValue();
        }
        return sum;
    }

    private ResponseEntity getMoney(List<Coin> myList) {
        int quantity;
        String formattedName;

        for (Coin coin : myList) {
            quantity = coin.getQuantity();

            if (quantity == 1) {
                formattedName = coin.getName();
            }else {
                formattedName = coin.getNameplural();
            }

            System.out.println( quantity + " " + formattedName);
        }

        System.out.println("The piggy bank holds " + checkSum(myList));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
