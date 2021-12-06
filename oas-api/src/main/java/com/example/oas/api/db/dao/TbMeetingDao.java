package com.example.oas.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbMeetingDao {
    public boolean searchMeetingMembersInSameDept(String uuid);
    public HashMap searchMeetingById(HashMap param);

    /**
     * 获取会议信息
     * sql语句很复杂
     * @param param 条件查询 例如 : userId, start, length
     * @return 集合
     */
    public ArrayList<HashMap> searchOfflineMeetingByPage(HashMap param);

    /**
     * 获取会议数量
     * @param param
     * @return rows
     */
    public long searchOfflineMeetingCount(HashMap param);
}