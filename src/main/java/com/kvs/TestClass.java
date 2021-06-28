package com.kvs;

import java.util.Random;

public class TestClass {
    public static void main(String[] args) {
        System.out.println("reached here");
        KeyValueStore ks = new KeyValueStore();
        ks.setKeyExpiryInSeconds(10);
        Random rand = new Random(); //instance of random class
        int upperbound = 10;
        //generate random values from 0-24
        for(int j=1;j<=10;j++) {
            for (int i = 1; i < 10; i++) {
                ks.put(""+j,i);

            }
        }
        for (int  k=1;k<=10;k++) {
            System.out.println("key s "+k+"val is "+ks.get(""+k));
        }
    }
}
