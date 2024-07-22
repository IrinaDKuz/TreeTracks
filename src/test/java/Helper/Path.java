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

    public static By penOnAdvertId(int advertId) {
        return By.xpath("//a[text()='" + advertId + "']/ancestor::tr//button");
    }
    public static By textIsEqual(String tag, String contain) {
        return By.xpath("//" + tag + "[text()=\""  + contain + "\"]");
    }

    public static By forPencilButton(String contain) {
        return By.xpath("(//td[text()='" + contain + "']/ancestor::tr//button)[1]");
    }
    public static By forPencilButtonNotes(String contain) {
        return By.xpath("(//div[text()='" + contain + "']/parent::div/parent::div//button)[1]");
    }

    public static By forEditButton(String contain) {
        return By.xpath("//div[text()='" + contain + "']/parent::div//button[text()='Edit']");
    }

    public static By forDeleteButton(String contain) {
        return By.xpath("//div[text()='" + contain + "']/parent::div//button[text()='Delete']");
    }
    public static By forBasketButton(String contain) {
        return By.xpath("(//td[text()='" + contain + "']/ancestor::tr//button)[2]");
    }
    public static By forBasketButtonNotes(String contain) {
        return By.xpath("(//div[text()='" + contain + "']/parent::div/parent::div//button)[2]");
    }

}
