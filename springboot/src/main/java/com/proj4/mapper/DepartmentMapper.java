package com.proj4.mapper;

import com.proj4.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepartmentMapper {

    List<Department> selectAll(Department department);

    @Select("SELECT * FROM DEPARTMENT WHERE id = #{id}")
    Department selectById(Integer id);

    void insert(Department department);

    void updateById(Department department);

    @Delete("DELETE FROM DEPARTMENT WHERE id = #{id}")
    void deleteById(Integer id);
}
