package com.proj4.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.proj4.entity.Account;
import com.proj4.entity.Admin;
import com.proj4.exception.CustomException;
import com.proj4.mapper.AdminMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        String account = admin.getAccount();
        Admin dbAdmin = adminMapper.selectByAccount(account);
        if(dbAdmin != null){
            throw new CustomException("500", "账号已存在,请更换别的账号");
        }
        if(StrUtil.isBlank((admin.getPassword()))){
            admin.setPassword("admin");
        }
        admin.setRole("admin");
        adminMapper.insert(admin);
    }



    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            this.deleteById(id);
        }
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }


    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }



    public PageInfo<Admin> selectPage(Admin admin,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }


    public Admin login(Account admin) {
        String account =  admin.getAccount();
        Admin dbAdmin =  adminMapper.selectByAccount(account);
        if (dbAdmin == null){
            throw new CustomException("500","账号不存在");
        }
        //数据库存在这个账号
        String password =  admin.getPassword();
        if(!dbAdmin.getPassword().equals(password)){//用户输入的密码与数据库的密码不匹配
            throw new CustomException("500","账号或密码错误,请重试");
        }
        return dbAdmin;
    }

    public Account updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectById(account.getId());
        if(!dbAdmin.getPassword().equals(account.getOldPassword())){
            throw new CustomException("500", "原密码错误");
        }else{
            adminMapper.updatePassword(account.getPassword(), account.getId());
        }
        return account;
    }
}
