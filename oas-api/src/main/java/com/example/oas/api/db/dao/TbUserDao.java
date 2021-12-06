package com.example.oas.api.db.dao;

import com.example.oas.api.db.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Mapper
public interface TbUserDao {
    public Set<String> searchUserPermissions(int userId);

    public HashMap searchById(int userId);

    public Integer searchIdByOpenId(String openId);

    public HashMap searchUserSummary(int userId);

    public HashMap searchUserInfo(int userId);

    public Integer searchDeptManagerId(int id);

    public Integer searchGmId();

    public ArrayList<HashMap> searchAllUser();

    /**
     * 完成登录
     *
     * @param param 用户名称 + 用户密码
     * @return 用户ID
     */
    public Integer login(HashMap param);

    /**
     * 完成修改密码操作
     *
     * @param param 用户ID + 新密码
     * @return 影响行数
     */
    public Integer updatePassword(HashMap param);

    /**
     * 完成弹性搜索获取关于用户的信息
     * @param param 搜索条件
     * @return 用户信息 格式 : <String, List>
     */
    public ArrayList<HashMap> searchUserByPage(HashMap param);

    /**
     * 完成弹性搜索下用户信息的rows
     * @param param 搜索条件
     * @return 用户信息行数
     */
    public long searchUserCount(HashMap param);

    /**
     * 完成用户添加
     * @param user
     * @return 影响行数
     */
    public int insert(TbUser user);

    /**
     * 用户信息修改
     * @param user
     * @return 影响行数
     */
    public int update(HashMap user);

    /**
     * 批量删除用户
     * @param ids userId集合
     * @return
     */
    public int deleteUserByIds(Integer[] ids);
}