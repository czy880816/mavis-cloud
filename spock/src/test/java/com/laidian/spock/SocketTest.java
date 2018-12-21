package com.laidian.spock;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class SocketTest {

    @Test
    public void testSocket() throws IOException {
        Assert.assertTrue(NetUtil.isPortInUse(8080));
        Assert.assertFalse(NetUtil.isPortInUse(8081));
    }

    static class NetUtil {
        /**
         * 对于实现AutoCloseable接口的资源强烈推荐使用try-with-resources控制结构
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
}
