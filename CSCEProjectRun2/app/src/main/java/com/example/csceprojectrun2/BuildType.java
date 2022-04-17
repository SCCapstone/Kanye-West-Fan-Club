package com.example.csceprojectrun2;

import java.io.Serializable;
import java.util.List;

public class BuildType implements Serializable {
    private final String name;
    private final List<PopularBuild2> popularBuild2s;

    public BuildType(String name, List<PopularBuild2> popularBuild2s) {
        this.name = name;
        this.popularBuild2s = popularBuild2s;
    }

    public String getName() {
        return name;
    }

    public List<PopularBuild2> getPopularBuilds() {
        return popularBuild2s;
    }
}