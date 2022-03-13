package com.example.csceprojectrun2;

import java.io.Serializable;

public class PopularBuild implements Serializable {
    String buildName;
    String unit1;
    String unit2;
    String unit3;
    String unit4;
    String unit5;
    String unit6;
    String unit7;
    String unit8;

    public PopularBuild(String buildName, String unit1, String unit2, String unit3, String unit4, String unit5, String unit6, String unit7, String unit8) {
        this.buildName = buildName;
        this.unit1 = unit1;
        this.unit2 = unit2;
        this.unit3 = unit3;
        this.unit4 = unit4;
        this.unit5 = unit5;
        this.unit6 = unit6;
        this.unit7 = unit7;
        this.unit8 = unit8;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getUnit1() {
        return unit1;
    }

    public String getUnit2() {
        return unit2;
    }

    public String getUnit3() {
        return unit3;
    }

    public String getUnit4() {
        return unit4;
    }

    public String getUnit5() {
        return unit5;
    }

    public String getUnit6() {
        return unit6;
    }

    public String getUnit7() {
        return unit7;
    }

    public String getUnit8() {
        return unit8;
    }
}