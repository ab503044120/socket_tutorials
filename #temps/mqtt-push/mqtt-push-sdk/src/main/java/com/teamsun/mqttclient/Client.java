package com.teamsun.mqttclient;

import com.teamsun.mqttclient.service.DefaultApiService;

/**
 * Created by Administrator on 2017/8/18.
 */

public class Client {
    public static void main(String[] args) {
        DefaultApiService defaultApiService = new DefaultApiService();
        defaultApiService.login("1","1","1");

    }
}
