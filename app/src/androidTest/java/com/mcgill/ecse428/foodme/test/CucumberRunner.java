package com.mcgill.ecse428.foodme.test;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "src/androidTest/assets/features",
        glue = {"com.mcgill.ecse428.foodme.test"},
        monochrome = true,
        plugin = { "pretty"}
)
public class CucumberRunner {
}