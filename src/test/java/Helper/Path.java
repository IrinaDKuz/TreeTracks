package Helper;

import org.openqa.selenium.By;

public class Path {

    public static By contain(String tag, String contain) {
        return By.xpath("//" + tag + "[contains(text()," + '\"' + contain + '\"' + ")]");
    }

    public static By contain(String tag, String contain, int number) {
        return By.xpath("(//" + tag + "[contains(text()," + '\"' + contain + '\"' + ")])[" + number + "]" );
    }

    public static String containPath(String tag, String contain) {
        return "//" + tag + "[contains(text()," + '\"' + contain + '\"' + ")]";
    }

    public static String getAncestor(String xpath, String tag) {
        return "(" + xpath + ")/ancestor::" + tag;
    }

    public static String getFollowingSibling(String xpath, String tag) {
        return "(" + xpath + ")/following-sibling::" + tag;
    }

    public static By textIsEqual(String tag, String contain) {
        return By.xpath("//" + tag + "[text()=" + '\"' + contain + '\"' +"]");
    }
}
