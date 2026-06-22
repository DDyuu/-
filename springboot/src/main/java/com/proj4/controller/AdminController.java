package com.proj4.controller;

import com.github.pagehelper.PageInfo;
import com.proj4.common.Result;
import com.proj4.entity.Admin;
import com.proj4.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Admin admin){
        adminService.updateById(admin);
        return Result.success(admin);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Admin admin){
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    @GetMapping("/selectById")
    public Result selectById(@RequestParam Integer id){
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
                             Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Admin> pageInfo = adminService.selectPage(admin,pageNum,pageSize);
        return Result.success(pageInfo);
    }

}
