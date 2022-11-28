package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.dto.*;
import com.ssafy.BravoSilverLife.entity.BDCode;
import com.ssafy.BravoSilverLife.entity.Bookmark;
import com.ssafy.BravoSilverLife.entity.User;
import com.ssafy.BravoSilverLife.repository.BDCodeRepository;
import com.ssafy.BravoSilverLife.repository.BookmarkRepository;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstateServiceImpl implements EstateService {

    @Autowired
    BDCodeRepository BDCodeRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Cluster> getClusters(Condition condition) throws Exception {

        List<BDCode> bdCodes = BDCodeRepository.findByName(condition.getDongName());

        String apiurl = "https://new.land.naver.com/api/articles/clusters?";
        apiurl += "cortarNo=" + bdCodes.get(0).getCode()
                + "&zoom=17&markerId&markerType&selectedComplexNo&selectedComplexBuildingNo&fakeComplexMarker&realEstateType=SG&tradeType=&tag=%3A%3A%3A%3A%3A%3A%3A%3A";
        apiurl += "&rentPriceMin=" + condition.getRentPriceMin();
        apiurl += "&rentPriceMax=" + condition.getRentPriceMax();
        apiurl += "&priceMin=" + condition.getPriceMin();
        apiurl += "&priceMax=" + condition.getPriceMax();
        apiurl += "&areaMin=" + condition.getAreaMin();
        apiurl += "&areaMax=" + condition.getAreaMax();
        apiurl += "&oldBuildYears&recentlyBuildYears&minHouseHoldCount&maxHouseHoldCount&showArticle=false&sameAddressGroup=true&minMaintenanceCost&maxMaintenanceCost&priceType=RETAIL&directions=";
        apiurl += "&leftLon=" + condition.getLeftLon();
        apiurl += "&rightLon=" + condition.getRightLon();
        apiurl += "&topLat=" + condition.getTopLat();
        apiurl += "&bottomLat=" + condition.getBottomLat();

        URL url = new URL(apiurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
        con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

        con.addRequestProperty("Accept", "*/*");
        con.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
        con.addRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        con.addRequestProperty("authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlJFQUxFU1RBVEUiLCJpYXQiOjE2NjY1ODU2NDYsImV4cCI6MTY2NjU5NjQ0Nn0.QR78-v3ZCzD4rnPTqY4UJBOChhlwplfxi7o71KXA9GY");
        con.addRequestProperty("Connection", "keep-alive");
        con.addRequestProperty("Cookie",
                "NNB=AA74EGYPWXFWE; m_loc=5728a2c06c053be3e4fabc814c4203f5af7e600fdf28feaab54175e103036df2; NV_WETR_LOCATION_RGN_M=\"MDIxMzE1NTA=\"; NV_WETR_LAST_ACCESS_RGN_M=\"MDIxMzE1NTA=\"; _ga=GA1.2.449519277.1661930757; _ga_7VKFYR6RV1=GS1.1.1663228241.4.0.1663228241.60.0.0; page_uid=hyYyasprvTVssDxnUgZssssssNK-359919; nhn.realestate.article.rlet_type_cd=A01; nhn.realestate.article.trade_type_cd=\"\"; nhn.realestate.article.ipaddress_city=2900000000; landHomeFlashUseYn=Y; realestate.beta.lastclick.cortar=2920000000; nid_inf=1427257442; NID_JKL=ycx3Zt23nCR3itEevK57FMMGtZbQqspe7ByT2/hYuIE=; BMR=s=1666585258383&r=https%3A%2F%2Fm.blog.naver.com%2Ffbfbf1%2F222632994331&r2=https%3A%2F%2Fwww.google.com%2F; REALESTATE=Mon%20Oct%2024%202022%2013%3A27%3A26%20GMT%2B0900%20(KST); wcs_bt=4f99b5681ce60:1666585646");
        con.addRequestProperty("Host", "new.land.naver.com");
        con.addRequestProperty("Referer", "https://new.land.naver.com/offices?ms=37.482968,127.0634,16&a=SG&e=RETAIL");

        con.setRequestMethod("GET");
        con.setDoOutput(true);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            // ???
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONParser parser = new JSONParser();

        List<Cluster> clusters = new ArrayList<>();
        JSONArray jsonArr = (JSONArray) parser.parse(response.toString());

        if (jsonArr != null) {
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject temp = (JSONObject) jsonArr.get(i);
                Cluster cluster = Cluster.builder()
                        .markerId(Long.parseLong((String) temp.get("markerId")))
                        .count((Long) temp.get("count"))
                        .latitude((Double) temp.get("latitude"))
                        .longitude((Double) temp.get("longitude"))
                        .leftLon((Double) temp.get("leftLon"))
                        .rightLon((Double) temp.get("rightLon"))
                        .bottomLat((Double) temp.get("bottomLat"))
                        .topLat((Double) temp.get("topLat"))
                        .build();

                clusters.add(cluster);
            }
        }
        return clusters;
    }

    @Override
    public ArticleList getArticles(long markerId, int page, Condition condition) throws Exception {

        String apiurl = "https://new.land.naver.com/api/articles?";
        apiurl += "markerId=" + markerId;
        apiurl += "&markerType=LGEOHASH_MIX_ARTICLE&prevScrollTop&order=rank&realEstateType=SG&tradeType=&tag=%3A%3A%3A%3A%3A%3A%3A%3A";
        apiurl += "&rentPriceMin=" + condition.getRentPriceMin();
        apiurl += "&rentPriceMax=" + condition.getRentPriceMax();
        apiurl += "&priceMin=" + condition.getPriceMin();
        apiurl += "&priceMax=" + condition.getPriceMax();
        apiurl += "&areaMin=" + condition.getAreaMin();
        apiurl += "&areaMax=" + condition.getAreaMax();
        apiurl += "&oldBuildYears&recentlyBuildYears&minHouseHoldCount&maxHouseHoldCount&showArticle=false&sameAddressGroup=true&minMaintenanceCost&maxMaintenanceCost&priceType=RETAIL&directions=&";
        apiurl += "page=" + page + "&articleState";

        URL url = new URL(apiurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
        con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

        con.addRequestProperty("Accept", "*/*");
        con.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
        con.addRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        con.addRequestProperty("authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlJFQUxFU1RBVEUiLCJpYXQiOjE2NjY1ODcxNTEsImV4cCI6MTY2NjU5Nzk1MX0.ECXQtzXedDfTHPx3ADfhQFIElBhzTrmdpHLOWCsTf-A");
        con.addRequestProperty("Connection", "keep-alive");
        con.addRequestProperty("Cookie",
                "NNB=AA74EGYPWXFWE; m_loc=5728a2c06c053be3e4fabc814c4203f5af7e600fdf28feaab54175e103036df2; NV_WETR_LOCATION_RGN_M=\"MDIxMzE1NTA=\"; NV_WETR_LAST_ACCESS_RGN_M=\"MDIxMzE1NTA=\"; _ga=GA1.2.449519277.1661930757; _ga_7VKFYR6RV1=GS1.1.1663228241.4.0.1663228241.60.0.0; page_uid=hyYyasprvTVssDxnUgZssssssNK-359919; nhn.realestate.article.rlet_type_cd=A01; nhn.realestate.article.trade_type_cd=\"\"; nhn.realestate.article.ipaddress_city=2900000000; landHomeFlashUseYn=Y; realestate.beta.lastclick.cortar=2920000000; nid_inf=1427257442; NID_JKL=ycx3Zt23nCR3itEevK57FMMGtZbQqspe7ByT2/hYuIE=; BMR=s=1666585258383&r=https%3A%2F%2Fm.blog.naver.com%2Ffbfbf1%2F222632994331&r2=https%3A%2F%2Fwww.google.com%2F; REALESTATE=Mon%20Oct%2024%202022%2013%3A52%3A31%20GMT%2B0900%20(KST); wcs_bt=4f99b5681ce60:1666587151");
        con.addRequestProperty("Host", "new.land.naver.com");
        con.addRequestProperty("Referer", "https://new.land.naver.com/offices?ms=37.482968,127.0634,16&a=SG&e=RETAIL");
        con.addRequestProperty("sec-ch-ua",
                "\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\"");
        con.addRequestProperty("sec-ch-ua-mobile", "?0");
        con.addRequestProperty("Sec-Fetch-Mode", "cors");
        con.addRequestProperty("sec-ch-ua-platform", "Windows");
        con.addRequestProperty("Sec-Fetch-Dest", "empty");
        con.addRequestProperty("Sec-Fetch-Site", "same-origin");
        con.addRequestProperty("User-Agent",
                " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");

        con.setRequestMethod("GET");
        con.setDoOutput(true);


        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            // ???
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(response.toString());
        boolean isMoreData = (boolean) jsonObj.get("isMoreData");
        List<Article> articles = new ArrayList<>();
        JSONArray jsonArr = (JSONArray) jsonObj.get("articleList");

        if (jsonArr != null) {
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject temp = (JSONObject) jsonArr.get(i);
                String[] floorInfo = ((String) temp.get("floorInfo")).split("/");
                if (temp.get("tradeTypeName").equals("매매")) {
                    Article article = Article.builder()
                            .articleNo(Long.parseLong((String) temp.get("articleNo")))
                            .articleName((String) temp.get("articleName"))
                            .tradeTypeName((String) temp.get("tradeTypeName"))
                            .floor(floorInfo[0])
                            .maxFloor(floorInfo[1])
                            .dealOrWarrantPrc((String) temp.get("dealOrWarrantPrc"))
                            .area1((Long) temp.get("area1"))
                            .area2((Long) temp.get("area2"))
                            .tagList((JSONArray) temp.get("tagList"))
                            .articleFeatureDesc((String) temp.get("articleFeatureDesc"))
                            .cpPcArticleUrl((String) temp.get("cpPcArticleUrl"))
                            .latitude((Double.parseDouble((String) temp.get("latitude"))))
                            .longitude((Double.parseDouble((String) temp.get("longitude"))))
                            .build();
                    articles.add(article);
                } else {
                    Article article = Article.builder()
                            .articleNo(Long.parseLong((String) temp.get("articleNo")))
                            .articleName((String) temp.get("articleName"))
                            .tradeTypeName((String) temp.get("tradeTypeName"))
                            .floor(floorInfo[0])
                            .maxFloor(floorInfo[1])
                            .rentPrc((String) temp.get("rentPrc"))
                            .dealOrWarrantPrc((String) temp.get("dealOrWarrantPrc"))
                            .area1((Long) temp.get("area1"))
                            .area2((Long) temp.get("area2"))
                            .tagList((JSONArray) temp.get("tagList"))
                            .articleFeatureDesc((String) temp.get("articleFeatureDesc"))
                            .cpPcArticleUrl((String) temp.get("cpPcArticleUrl"))
                            .latitude((Double.parseDouble((String) temp.get("latitude"))))
                            .longitude((Double.parseDouble((String) temp.get("longitude"))))
                            .build();
                    articles.add(article);
                }

            }
        }

        ArticleList articleList = ArticleList.builder()
                .isMoreData(isMoreData)
                .articles(articles)
                .build();

        return articleList;
    }

    @Override
    public ArticleDetail getArticleDetail(long articleNo) throws Exception {

        String apiurl = "https://new.land.naver.com/api/articles/";
        apiurl += articleNo;
        apiurl += "?complexNo=";

        URL url = new URL(apiurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
        con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정

        con.addRequestProperty("Accept", "*/*");
        con.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
        con.addRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        con.addRequestProperty("authorization",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IlJFQUxFU1RBVEUiLCJpYXQiOjE2NjY1OTM0NDksImV4cCI6MTY2NjYwNDI0OX0.rksKZp_aYP7bu7pBTZti_x4LjcfDcGNZNtc1hMTriJ8");
        con.addRequestProperty("Connection", "keep-alive");
        con.addRequestProperty("Cookie",
                "NNB=AA74EGYPWXFWE; m_loc=5728a2c06c053be3e4fabc814c4203f5af7e600fdf28feaab54175e103036df2; NV_WETR_LOCATION_RGN_M=\"MDIxMzE1NTA=\"; NV_WETR_LAST_ACCESS_RGN_M=\"MDIxMzE1NTA=\"; _ga=GA1.2.449519277.1661930757; _ga_7VKFYR6RV1=GS1.1.1663228241.4.0.1663228241.60.0.0; page_uid=hyYyasprvTVssDxnUgZssssssNK-359919; nhn.realestate.article.rlet_type_cd=A01; nhn.realestate.article.trade_type_cd=\"\"; nhn.realestate.article.ipaddress_city=2900000000; landHomeFlashUseYn=Y; realestate.beta.lastclick.cortar=2920000000; nid_inf=1427257442; NID_JKL=ycx3Zt23nCR3itEevK57FMMGtZbQqspe7ByT2/hYuIE=; BMR=s=1666585258383&r=https%3A%2F%2Fm.blog.naver.com%2Ffbfbf1%2F222632994331&r2=https%3A%2F%2Fwww.google.com%2F; _gid=GA1.2.1919873049.1666593397; REALESTATE=Mon%20Oct%2024%202022%2015%3A37%3A29%20GMT%2B0900%20(KST); wcs_bt=4f99b5681ce60:1666593449");
        con.addRequestProperty("Host", "new.land.naver.com");
        con.addRequestProperty("Referer",
                "https://new.land.naver.com/offices?ms=37.4823891,127.0940201,16&a=SG&e=RETAIL");
        con.addRequestProperty("sec-ch-ua",
                "\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\"");
        con.addRequestProperty("sec-ch-ua-mobile", "?0");
        con.addRequestProperty("Sec-Fetch-Mode", "cors");
        con.addRequestProperty("sec-ch-ua-platform", "Windows");
        con.addRequestProperty("Sec-Fetch-Dest", "empty");
        con.addRequestProperty("Sec-Fetch-Site", "same-origin");
        con.addRequestProperty("User-Agent",
                " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");

        con.setRequestMethod("GET");
        con.setDoOutput(true);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            // ???
        }

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(response.toString());
        System.out.println(jsonObj);

        JSONObject articleDetail = (JSONObject) jsonObj.get("articleDetail");
        JSONObject articleAddition = (JSONObject) jsonObj.get("articleAddition");
        JSONObject articlePrice = (JSONObject) jsonObj.get("articlePrice");
        JSONArray tagList = (JSONArray) articleDetail.get("tagList");
        JSONArray articlePhotos = (JSONArray) jsonObj.get("articlePhotos");
        String[] floorInfo = ((String) articleAddition.get("floorInfo")).split("/");

        String rentPrice = null;


        ArticleDetail aD;
        if (articleDetail.get("monthlyManagementCost") == null) {
            aD = ArticleDetail.builder()
                    .articleNo((Long.parseLong((String) articleDetail.get("articleNo"))))
                    .articleName((String) articleDetail.get("articleName"))
                    .cortarNo((Long.parseLong((String) articleDetail.get("cortarNo"))))
                    .buildingTypeName((String) articleDetail.get("buildingTypeName"))
                    .tradeTypeName((String) articleDetail.get("tradeTypeName"))
                    .latitude((Double.parseDouble((String) articleDetail.get("latitude"))))
                    .longitude((Double.parseDouble((String) articleDetail.get("longitude"))))
                    .cityName((String) articleDetail.get("cityName"))
                    .divisionName((String) articleDetail.get("divisionName"))
                    .sectionName((String) articleDetail.get("sectionName"))
                    .walkingTimeToNearSubway((Long) articleDetail.get("walkingTimeToNearSubway"))
                    .exposureAddress((String) articleDetail.get("exposureAddress"))
                    .articleFeatureDescription((String) articleDetail.get("articleFeatureDescription"))
                    .detailDescription((String) articleDetail.get("detailDescription"))
                    .parkingCount((Long) articleDetail.get("parkingCount"))
                    .parkingPossibleYN((String) articleDetail.get("parkingPossibleYN"))
                    .tagList(tagList)
                    .floor(floorInfo[0])
                    .maxFloor(floorInfo[1])
                    .area1((Long) articleAddition.get("area1"))
                    .area2((Long) articleAddition.get("area2"))
                    .direction((String) articleAddition.get("direction"))
                    .buildingName((String) articleAddition.get("buildingName"))
                    .cpPcArticleUrl((String) articleAddition.get("cpPcArticleUrl"))
                    .rentPrice((Long) articlePrice.get("rentPrice"))
                    .dealPrice((Long) articlePrice.get("dealPrice"))
                    .warrantPrice((Long) articlePrice.get("warrantPrice"))
                    .articlePhotos(articlePhotos)
                    .build();
        } else {
            aD = ArticleDetail.builder()
                    .articleNo((Long.parseLong((String) articleDetail.get("articleNo"))))
                    .articleName((String) articleDetail.get("articleName"))
                    .cortarNo((Long.parseLong((String) articleDetail.get("cortarNo"))))
                    .buildingTypeName((String) articleDetail.get("buildingTypeName"))
                    .tradeTypeName((String) articleDetail.get("tradeTypeName"))
                    .latitude((Double.parseDouble((String) articleDetail.get("latitude"))))
                    .longitude((Double.parseDouble((String) articleDetail.get("longitude"))))
                    .cityName((String) articleDetail.get("cityName"))
                    .divisionName((String) articleDetail.get("divisionName"))
                    .sectionName((String) articleDetail.get("sectionName"))
                    .walkingTimeToNearSubway((Long) articleDetail.get("walkingTimeToNearSubway"))
                    .exposureAddress((String) articleDetail.get("exposureAddress"))
                    .monthlyManagementCost((Long) articleDetail.get("monthlyManagementCost"))
                    .articleFeatureDescription((String) articleDetail.get("articleFeatureDescription"))
                    .detailDescription((String) articleDetail.get("detailDescription"))
                    .parkingCount((Long) articleDetail.get("parkingCount"))
                    .parkingPossibleYN((String) articleDetail.get("parkingPossibleYN"))
                    .tagList(tagList)
                    .floor(floorInfo[0])
                    .maxFloor(floorInfo[1])
                    .area1((Long) articleAddition.get("area1"))
                    .area2((Long) articleAddition.get("area2"))
                    .direction((String) articleAddition.get("direction"))
                    .buildingName((String) articleAddition.get("buildingName"))
                    .cpPcArticleUrl((String) articleAddition.get("cpPcArticleUrl"))
                    .rentPrice((Long) articlePrice.get("rentPrice"))
                    .dealPrice((Long) articlePrice.get("dealPrice"))
                    .warrantPrice((Long) articlePrice.get("warrantPrice"))
                    .articlePhotos(articlePhotos)
                    .build();
        }


        return aD;
    }

    @Override
    public void addBookmark(String id, BookmarkDto bookmark) {
        User user = userRepository.findById(id);
        System.out.println(bookmark);
        Bookmark bm = Bookmark.builder()
                .user(user)
                .address(bookmark.getAddress())
                .price(bookmark.getPrice())
                .articleNo(bookmark.getArticleNo())
                .month(bookmark.getMonth())
                .url(bookmark.getUrl())
                .build();

        bookmarkRepository.save(bm);
    }

    @Override
    public void deleteBookmark(String id, long articleNo) {
        User user = userRepository.findById(id);
        bookmarkRepository.deleteByUserAndArticleNo(user, articleNo);
    }

    @Override
    public List<BookmarkDto> getBookmark(String id) throws Exception {
        User user = userRepository.findById(id);
        System.out.println("@@@@@: " + user.getIdx());
        List<Bookmark> bookmarks = bookmarkRepository.findAll();
        List<BookmarkDto> bookmarkDtos = new ArrayList<>();

        for (Bookmark temp : bookmarks) {
            if (temp.getUser().getId().equals(id)) {
                bookmarkDtos.add(BookmarkDto.of(temp));
            }
        }
        return bookmarkDtos;
    }
}
