package com.proj4.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.proj4.entity.Account;
import com.proj4.entity.Employee;
import com.proj4.exception.CustomException;
import com.proj4.mapper.EmployeeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    public void add(Employee employee) {
        employeeMapper.insert(employee);
    }



    public void updateById(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            this.deleteById(id);
        }
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }


    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }



    public PageInfo<Employee> selectPage(Employee employee,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }


    public Employee login(Account employee) {
        String account =  employee.getAccount();
        Employee dbEmployee =  employeeMapper.selectByAccount(account);
        if (dbEmployee == null){
            throw new CustomException("500","账号不存在");
        }
        //数据库存在这个账号
        String password =  employee.getPassword();
        if(!dbEmployee.getPassword().equals(password)){//用户输入的密码与数据库的密码不匹配
            throw new CustomException("500","账号或密码错误,请重试");
        }
        return dbEmployee;
    }

    public void Register(Employee employee) {
        String account =  employee.getAccount();
        Employee dbEmployee =  employeeMapper.selectByAccount(account);
        if(dbEmployee != null){
            throw new CustomException("500","账号已存在,请更换");
        }
        if(StrUtil.isBlank(employee.getPassword())){
            employee.setPassword("123");
        }
        if(StrUtil.isBlank(employee.getName())){
            employee.setName(employee.getAccount());
        }
        employee.setRole("employee");

        this.add(employee);
    }

    public Account updatePassword(Account account) {
        Employee dbEmployee = employeeMapper.selectById(account.getId());
        if(!dbEmployee.getPassword().equals(account.getOldPassword())){
            throw new CustomException("500", "原密码错误");
        }else{
            employeeMapper.updatePassword(account.getPassword(), account.getId());
        }
        return account;
    }
}
