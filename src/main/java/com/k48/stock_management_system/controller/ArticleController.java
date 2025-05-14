package com.k48.stock_management_system.controller;


import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private  ArticleService articleService;





    @PostMapping("/")
    public  ResponseEntity<?> saveArticle(@RequestBody ArticleDto  articleDto){
        return ResponseEntity.ok(articleService.save(articleDto));
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> deleteArticleById(@PathVariable Integer articleId){
        return ResponseEntity.ok(articleService.deleteById(articleId));
    }

    @GetMapping("/{codeArticle}")
    public ResponseEntity<?> findByCodeArticle(@PathVariable String codeArticle){
        return ResponseEntity.ok(articleService.findByCodeArticle(codeArticle));
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(articleService.findAll());
    }




}
