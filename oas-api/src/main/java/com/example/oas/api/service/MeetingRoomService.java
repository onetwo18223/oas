package com.example.oas.api.service;

import com.example.oas.api.common.util.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;

public interface MeetingRoomService {
    public ArrayList<HashMap> searchAllMeetingRoom();

    public HashMap searchById(int id);

    public ArrayList<String> searchFreeMeetingRoom(HashMap param);

    public PageUtils searchMeetingRoomByPage(HashMap param);
}
