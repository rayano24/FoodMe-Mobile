package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class FilterPriceStepDefs {

    private static long timeout = 1000;

    public static void when(Solo solo, String price) throws Exception{
        CucumberActionSteps.filterByPrice(solo, price);
    }

    public static boolean then(Solo solo, String price) {

        boolean  ret = false;

        switch (price) {
            case "$":
                ret = !(solo.searchText("$$", 1, true, true)
                        || solo.searchText("$$$",1, true, true)
                        || solo.searchText("$$$$", 1, true, true));
                break;
            case "$$":
                ret =  !(solo.searchText("$", 1, true, true)
                        || solo.searchText("$$$", 1, true, true)
                        || solo.searchText("$$$$", 1, true, true));
                break;
            case "$$$":
                ret =  !(solo.searchText("$", 1, true, true)
                        || solo.searchText("$$", 1, true, true)
                        || solo.searchText("$$$$", 1, true, true));
                break;
            case "$$$$":
                ret =  !(solo.searchText("$", 1, true, true)
                        || solo.searchText("$$", 1, true, true)
                        || solo.searchText("$$$", 1, true, true));
                break;
        }

        return ret;
    }
}
