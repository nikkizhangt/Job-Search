package com.example.jobsearch.recommendation;

import com.example.jobsearch.db.MySQLConnection;
import com.example.jobsearch.entity.Item;
import com.example.jobsearch.external.SerpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class Recommendation {
    public List<Item> recommendationItems(String userId, double lat, double lon) {
        List<Item> recommendationItems = new ArrayList<>();

        //Step1, get all favorited itemids
        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);

        //Step 2, get all keywords, sort by count
        //{"software engineer": 6, "backend": 4, "san francisco": 3,
        //    "remote": 1}
        Map<String, Integer> allKeywords = new HashMap<>();
        for (String itemId : favoriteItemIds) {
            Set<String> keywords = connection.getKeywords(itemId);
            for (String keyword: keywords) {
                allKeywords.put(keyword, allKeywords.getOrDefault(keyword,0) + 1);
            }
        }
        connection.close();
        List<Map.Entry<String,Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
        keywordList.sort((Map.Entry<String,Integer> e1, Map.Entry<String, Integer> e2) -> Integer.compare(e2.getValue(), e1.getValue()));
        // Cut down search list only top 3
        if (keywordList.size() >  3) {
            keywordList = keywordList.subList(0,3);
        }
        // Step 3, search based on keywords, filter out favorite items

        Set<String> visitedItemIds = new HashSet<>();
        SerpClient client = new SerpClient();
        for (Map.Entry<String, Integer> keyword: keywordList) {
            List<Item> items = client.search(lat, lon, keyword.getKey());
            for (Item item:items) {
                if (!favoriteItemIds.contains(item.getId()) && !visitedItemIds.contains(item.getId())) {
                    recommendationItems.add(item);
                    visitedItemIds.add(item.getId());
                }
            }
        }
        return recommendationItems;


    }
}
