package com.ssafy.BravoSilverLife.controller;

import com.ssafy.BravoSilverLife.dto.*;
import com.ssafy.BravoSilverLife.entity.Bookmark;
import com.ssafy.BravoSilverLife.service.EstateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estate", description = "EstateAPI")
@RestController
@CrossOrigin("*")
@RequestMapping("/v1/estate")
public class EstateController {

    @Autowired
    EstateService estateService;

    @Operation(summary = "매물 클러스터", description = "좌표로 매물 클러스터를 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/clusters")
    public ResponseEntity getClusters(Condition condition) throws Exception {

        List<Cluster> clusters = estateService.getClusters(condition);
        return ResponseEntity.status(200).body(clusters);
    }

    @Operation(summary = "클러스터 내 매물 확인", description = "클러스터 내 매물 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/articles")
    public ResponseEntity getArticles(long markerId, int page, Condition condition) throws Exception {
        ArticleList articleList = estateService.getArticles(markerId, page, condition);
        return ResponseEntity.status(200).body(articleList);
    }

    @Operation(summary = "매물 상세 확인", description = "매물 상세 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/article-detail")
    public ResponseEntity getArticleDetail(long articleNo) throws Exception {
        ArticleDetail articleDetail = estateService.getArticleDetail(articleNo);
        return ResponseEntity.status(200).body(articleDetail);

    }

//    @Operation(summary = "북마크 확인", description = "북마크 확인하는 API")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공"),
//    })
//    @GetMapping("/check-bookmarks")
//    public ResponseEntity isBookmarks(String id, String articleNo) throws Exception {
//       List<Bookmark> bookmarks = estateService.getBookmark(id);
//        return ResponseEntity.status(200).body("temp");
//
//    }

    @Operation(summary = "매물 북마크", description = "매물 북마크하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/article-bookmark")
    public ResponseEntity addBookmark(String id, BookmarkDto bookmark) throws Exception {
        System.out.println("123");
        estateService.addBookmark(id, bookmark);
        System.out.println("312");
        return ResponseEntity.status(200).body("북마크 추가");
    }

    @Operation(summary = "매물 북마크 취소", description = "매물 북마크 취소하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @DeleteMapping("/article-bookmark")
    public ResponseEntity deleteBookmark(String id, long articleNo) throws Exception {
        estateService.deleteBookmark(id,articleNo);
        return ResponseEntity.status(200).body("북마크 취소");

    }

    @Operation(summary = "북마크 리스트 확인", description = "북마크 리스트 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/bookmarks")
    public ResponseEntity getBookmarks(String id) throws Exception {
       List<BookmarkDto> bookmarks = estateService.getBookmark(id);
        return ResponseEntity.status(200).body(bookmarks);
    }





}
