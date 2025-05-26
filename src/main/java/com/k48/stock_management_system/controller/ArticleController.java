package com.k48.stock_management_system.controller;


import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;



@RestController
@RequestMapping(APP_ROOT+"/article")
@RequiredArgsConstructor
public class ArticleController {

    private  ArticleService articleService;



    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> saveArticle(@RequestBody ArticleDto  articleDto){
        return ResponseEntity.ok(articleService.save(articleDto));
    }

    @GetMapping(value = "/findbycode/{codeArticle}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByCodeArticle(@PathVariable String codeArticle){
        return ResponseEntity.ok(articleService.findByCodeArticle(codeArticle));
    }

    @GetMapping("/findall")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(articleService.findAll());
    }


    @DeleteMapping(value = "/delete/{articleId}")
    public ResponseEntity<?> deleteArticleById(@PathVariable Integer articleId){
        return ResponseEntity.ok(articleService.delete(articleId));
    }


}
