package com.example.oas.api.service.impl;

import com.example.oas.api.common.util.PageUtils;
import com.example.oas.api.db.dao.TbMeetingRoomDao;
import com.example.oas.api.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    @Autowired
    private TbMeetingRoomDao meetingRoomDao;

    @Override
    public ArrayList<HashMap> searchAllMeetingRoom() {
        ArrayList<HashMap> list = meetingRoomDao.searchAllMeetingRoom();
        return list;
    }

    @Override
    public HashMap searchById(int id) {
        HashMap map = meetingRoomDao.searchById(id);
        return map;
    }

    @Override
    public ArrayList<String> searchFreeMeetingRoom(HashMap param) {
        ArrayList<String> list = meetingRoomDao.searchFreeMeetingRoom(param);
        return list;
    }

    @Override
    public PageUtils searchMeetingRoomByPage(HashMap param) {
        ArrayList<HashMap> list = meetingRoomDao.searchMeetingRoomByPage(param);
        long count = meetingRoomDao.searchMeetingRoomCount(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }
}