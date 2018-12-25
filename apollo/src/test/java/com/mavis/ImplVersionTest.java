package com.mavis;

import com.mavis.apollo.demo.api.ApolloConfigDemo;
import org.junit.Test;

public class ImplVersionTest {
    @Test
    public void testVersion() {
        String version = ApolloConfigDemo.class.getPackage().getImplementationVersion();
        System.out.println(version);
    }
}
