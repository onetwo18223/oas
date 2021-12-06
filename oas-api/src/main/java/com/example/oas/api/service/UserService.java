package com.example.oas.api.service;

import com.example.oas.api.common.util.PageUtils;
import com.example.oas.api.db.pojo.TbUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface UserService {
    public HashMap createQrCode();

//    public boolean checkQrCode(String code,String uuid);

    public HashMap wechatLogin(String uuid);

    public Set<String> searchUserPermissions(int userId);

    public HashMap searchUserSummary(int userId);

    public HashMap searchById(int userId);

    public ArrayList<HashMap> searchAllUser();

    /**
     * 完成登录功能
     * @param param 用户名 + 用户密码
     * @return 用户ID
     */
    public Integer login(HashMap param);

    /**
     * 完成修改操作
     * @param param userId + 新密码
     * @return 影响的行数
     */
    public Integer updatePassword(HashMap param);

    /**
     * 弹性搜索用户信息
     * @param param 搜索条件
     * @return PageUtils
     */
    public PageUtils searchUserByPage(HashMap param);

    /**
     * 添加用户
     * @param param 用户信息
     * @return 影响行数
     */
    public int insert(TbUser param);

    /**
     * 修改用户信息
     * @param param 用户信息,
     *              使用的是HashMap而不是TbUser是因为TbUser是id, 而form中的userId
     *              需要完成多余转换
     *              且除了添加操作使用HashMap更好一点
     * @return 影响行数
     */
    public int update(HashMap param);

    /**
     * 批量删除用户信息
     * @param ids 用户ID集合
     * @return 影响行数
     */
    public int deleteUserByIds(Integer[] ids);
}
