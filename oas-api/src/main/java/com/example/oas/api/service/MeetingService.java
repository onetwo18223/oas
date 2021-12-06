package com.example.oas.api.service;

import com.example.oas.api.common.util.PageUtils;

import java.util.HashMap;

public interface MeetingService {
    public PageUtils searchOfflineMeetingByPage(HashMap param);
}
