package com.proj4.controller;

import com.github.pagehelper.PageInfo;
import com.proj4.common.Result;
import com.proj4.service.EmployeeService;
import com.proj4.entity.Employee;

import java.io.InputStream;
import java.net.URLEncoder;
import cn.hutool.poi.excel.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/add")
    public Result add(@RequestBody Employee employee){
        employeeService.add(employee);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return Result.success(employee);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        employeeService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        employeeService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
                             Employee employee,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        List<Employee> employeeList = employeeService.selectAll(null);
        //将employeeList转换为Excel文件
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置中文表头
        writer.addHeaderAlias("id","序号");
        writer.addHeaderAlias("account","账号");
        writer.addHeaderAlias("role","角色");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("no","工号");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("description","描述");
        writer.addHeaderAlias("departmentId","部门ID");
        writer.setOnlyAlias(true);
        writer.write(employeeList, true);
        //设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工列表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        writer.flush(response.getOutputStream());
        writer.close();
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception{
        //拿到输入流
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //读取数据
        reader.addHeaderAlias("序号","id");
        reader.addHeaderAlias("账号","account");
        reader.addHeaderAlias("角色","role");
        reader.addHeaderAlias("姓名","name");
        reader.addHeaderAlias("性别","sex");
        reader.addHeaderAlias("工号","no");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("描述","description");
        reader.addHeaderAlias("部门ID","departmentId");
        List<Employee> employeeList = reader.readAll(Employee.class);
        //写入list数据到数据库
        for(Employee employee : employeeList){
            employeeService.add(employee);
        }
        return Result.success();
    }

}
