package com.mavis.apollo.demo.api;

public class Version {
    public static void main(String[] args) {
        String version = Version.class.getPackage().getImplementationVersion();
        System.out.println(version);
    }
}
