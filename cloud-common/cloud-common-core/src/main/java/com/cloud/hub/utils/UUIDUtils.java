package com.cloud.hub.utils;

import java.util.UUID;

/**
 * @Author: jaxMine
 * @Date: 2019/12/31 16:48
 */
public class UUIDUtils {
    public static String genUUid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }
}
