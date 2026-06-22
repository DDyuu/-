package com.proj4.mapper;

import com.proj4.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper {


    List<Article> selectAll(Article article);

    @Select("SELECT * FROM `ADMIN` WHERE id = #{id}")
    Article selectById(Integer id);

    void insert(Article article);

    void updateById(Article article);

    @Delete("DELETE FROM `ADMIN` WHERE id = #{id}")
    void deleteById(Integer id);
}
