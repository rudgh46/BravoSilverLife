package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.dto.ArticleList;
import com.ssafy.BravoSilverLife.dto.Cluster;
import com.ssafy.BravoSilverLife.dto.Condition;
import org.json.simple.JSONObject;

import java.util.List;

public interface InfraService {
    JSONObject getPopular(String name) throws Exception;

    List<String> getDistinctGugun() throws Exception;
    List<String> getDong(String gugun) throws Exception;
}
