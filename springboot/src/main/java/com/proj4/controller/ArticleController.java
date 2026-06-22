package com.proj4.controller;

import com.github.pagehelper.PageInfo;
import com.proj4.common.Result;
import com.proj4.entity.Article;
import com.proj4.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody Article article){
        articleService.add(article);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Article article){
        articleService.updateById(article);
        return Result.success(article);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        articleService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        articleService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Article article){
        List<Article> list = articleService.selectAll(article);
        return Result.success(list);
    }

    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id){
        Article article = articleService.selectById(id);
        return Result.success(article);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            Article article,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Article> pageInfo = articleService.selectPage(article,pageNum,pageSize);
        return Result.success(pageInfo);
    }

}
