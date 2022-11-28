package com.ssafy.BravoSilverLife.service;

import com.ssafy.BravoSilverLife.dto.*;
import com.ssafy.BravoSilverLife.entity.Bookmark;

import java.util.List;

public interface EstateService {

    List<Cluster> getClusters(Condition condition) throws Exception;

    ArticleList getArticles(long markerId, int page, Condition condition) throws Exception;

    ArticleDetail getArticleDetail(long articleNo) throws Exception;

    void addBookmark(String id, BookmarkDto bookmark);
    void deleteBookmark(String id, long articleNo);

    List<BookmarkDto> getBookmark(String id) throws Exception;

}
