package com.example.fruitdirectselling.fruitlistview;

import java.util.ArrayList;
import java.util.List;

public class SearchFruitsUtil {

    public static List<FruitInfo> searchFruitsByName(String name, List<FruitInfo> fruits){
        List<FruitInfo> searchFruits = new ArrayList<>();
        int len = fruits.size();
        for(int i = 0; i < len; i++){
            FruitInfo fruit = fruits.get(i);
            String name2 = fruit.getName();
            if(name2.indexOf(name) != -1){
                searchFruits.add(fruit);
            }
        }
        return searchFruits;
    }

    public static List<FruitInfo> searchFruitsByOriginPlace(String originPlace, List<FruitInfo> fruits){
        List<FruitInfo> searchFruits = new ArrayList<>();
        int len = fruits.size();
        for(int i = 0; i < len; i++){
            FruitInfo fruit = fruits.get(i);
            String originPlace2 = fruit.getOrginplace();
            if(originPlace2.indexOf(originPlace) != -1){
                searchFruits.add(fruit);
            }
        }
        return searchFruits;
    }
}
