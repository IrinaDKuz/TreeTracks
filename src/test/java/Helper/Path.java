package Helper;

import org.openqa.selenium.By;

public class Path {

    public static By contain(String tag, String contain) {
        return By.xpath("//" + tag + "[contains(text()," + '\"' + contain + '\"' + ")]");
    }


    public static By elementContainElement(String tag1, String contain1, String tag2, String contain2) {
        return By.xpath("//" + tag1 + "[contains(text()," + '\"' + contain1 + '\"' + ")]" +
                "//" + tag2 + "[contains(text()," + '\"' + contain2 + '\"' + ")]");
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
    public static By forEditDeleteButtons(String tag, String contain, String buttonName) {
        return By.xpath("//" + tag + "[contains(text(),'" + contain +
                        "')]/parent::div/parent::div/parent::div//button[contains(text(),'" + buttonName + "')]");
    }

}
