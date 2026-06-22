package com.proj4.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.proj4.entity.Article;
import com.proj4.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    public void add(Article article) {
        article.setDate(DateUtil.date());
        articleMapper.insert(article);
    }



    public void updateById(Article article) {
        articleMapper.updateById(article);
    }

    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            this.deleteById(id);
        }
    }

    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }


    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }



    public PageInfo<Article> selectPage(Article article,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

}
