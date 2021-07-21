package com.omrs;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("abc")
public class TestOmrsConfigGradle extends Application {
    @PostConstruct
    public void init() {
    	System.out.println("initialized");
    }

}

