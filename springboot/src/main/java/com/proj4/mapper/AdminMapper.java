package com.proj4.mapper;

import com.proj4.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AdminMapper {


    List<Admin> selectAll(Admin admin);

    @Select("SELECT * FROM `ADMIN` WHERE id = #{id}")
    Admin selectById(Integer id);

    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("DELETE FROM `ADMIN` WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM `ADMIN` WHERE account = #{account}")
    Admin selectByAccount(String account);

    @Update("UPDATE `ADMIN` SET password = #{password} WHERE id = #{id}")
    void updatePassword(String password, Integer id);
}
