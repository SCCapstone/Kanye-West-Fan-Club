package com.example.csceprojectrun2;

import java.io.Serializable;
import java.util.List;

public class BuildType implements Serializable {
    private final String name;
    private final List<PopularBuild> popularBuilds;

    public BuildType(String name, List<PopularBuild> popularBuilds) {
        this.name = name;
        this.popularBuilds = popularBuilds;
    }

    public String getName() {
        return name;
    }

    public List<PopularBuild> getPopularBuilds() {
        return popularBuilds;
    }
}