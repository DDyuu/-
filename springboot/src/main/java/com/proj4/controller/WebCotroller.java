package com.proj4.controller;

import com.proj4.common.Result;
import com.proj4.entity.Account;
import com.proj4.entity.Article;
import com.proj4.entity.Employee;
import com.proj4.service.AdminService;
import com.proj4.service.ArticleService;
import com.proj4.service.EmployeeService;
import jakarta.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;



@RestController
public class WebCotroller {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;
    @Resource
    private ArticleService articleService;

    @GetMapping("/hello")
    public Result hello(){
        return Result.success("Hello");
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account){
        Account result = null;
        if("admin".equals(account.getRole())){
            result = adminService.login(account);
        }else if("employee".equals(account.getRole())){
            result = employeeService.login(account);
        }
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Employee employee){
        employeeService.Register(employee);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        Account result = null;
        if("admin".equals(account.getRole())){
            result = adminService.updatePassword(account);
        }else if("employee".equals(account.getRole())){
            result = employeeService.updatePassword(account);
        }
        return Result.success(result);
    }

    @GetMapping("/barData")
    public Result getBarData(){
        List<Employee> employeeList = employeeService.selectAll(null);
        // 统计每个部门的员工数量
        Map<String, Long> departmentCount = employeeList.stream()
                .filter(e -> e.getDepartmentName() != null)
                .collect(Collectors.groupingBy(Employee::getDepartmentName, Collectors.counting()));
        // 组装返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("department", new ArrayList<>(departmentCount.keySet()));
        result.put("count", new ArrayList<>(departmentCount.values()));
        return Result.success(result);
    }

    @GetMapping("/lineData")
    public Result getLineData(){
        List<Article> articleList = articleService.selectAll(null);
        // 按日期分组统计文章数量（精确到天）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Long> articleCount = articleList.stream()
                .filter(a -> a.getDate() != null)
                .collect(Collectors.groupingBy(a -> sdf.format(a.getDate()), Collectors.counting()));
        // 按日期排序
        TreeMap<String, Long> sortedMap = new TreeMap<>(articleCount);
        Map<String, Object> result = new HashMap<>();
        result.put("date", new ArrayList<>(sortedMap.keySet()));
        result.put("count", new ArrayList<>(sortedMap.values()));
        return Result.success(result);
    }

    @GetMapping("/pieData")
    public Result getPieData(){
        List<Employee> employeeList = employeeService.selectAll(null);
        // 统计每个部门的员工数量
        Map<String, Long> departmentCount = employeeList.stream()
                .filter(e -> e.getDepartmentName() != null)
                .collect(Collectors.groupingBy(Employee::getDepartmentName, Collectors.counting()));
        // 组装饼图数据 [{name: '技术部', value: 8}, ...]
        List<Map<String, Object>> pieList = departmentCount.entrySet().stream().map(entry -> {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("value", entry.getValue());
            return item;
        }).collect(Collectors.toList());
        return Result.success(pieList);
    }
}
