package com.laidian.common.util;

import java.net.Socket;

public class NetUtil {
    /**
     * 对于实现AutoCloseable接口的资源强烈推荐使用try-with-resources控制结构
     *
     * @param port
     * @return
     */
    public static boolean isPortInUse(int port) {
        try (Socket socket = new Socket("127.0.0.1", port)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}