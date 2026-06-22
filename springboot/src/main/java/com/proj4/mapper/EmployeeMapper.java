package com.proj4.mapper;

import com.proj4.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface EmployeeMapper {


    List<Employee> selectAll(Employee employee);

    @Select("SELECT * FROM `EMPLOYEE` WHERE id = #{id}")
    Employee selectById(Integer id);

    void insert(Employee employee);

    void updateById(Employee employee);

    @Delete("DELETE FROM `EMPLOYEE` WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM `EMPLOYEE` WHERE account = #{account}")
    Employee selectByAccount(String account);

    @Update("UPDATE `EMPLOYEE` SET password = #{password} WHERE id = #{id}")
    void updatePassword(String password, Integer id);
}
