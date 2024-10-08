package Helper;

import org.openqa.selenium.By;

public class Buttons {
    public static final By BACK = By.xpath("//button[contains(text(), ' Back')]");
    public static final By BACK_RU = By.xpath("//button[contains(text(), 'Назад')]");
    public static final By A_BACK_RU = By.xpath("//a[contains(text(), 'Назад')]");

    public static final By ADD = By.xpath("//button[contains(text(), 'Add')]");
    public static final By ADD_RU = By.xpath("//button[contains(text(), 'Добавить')]");
    public static final By A_ADD_RU = By.xpath("//a[contains(text(), 'Добавить')]");

    public static final By UPDATE = By.xpath("//button[contains(text(), 'Update')]");
    public static final By SAVE = By.xpath("//button[contains(text(), 'Сохранить')]");
    public static final By CREATE = By.xpath("//button[contains(text(), 'Создать')]");

    public static final By SUCCESS_BUTTON = By.xpath("//button[@class ='btn btn-success']");
    public static final By SUCCESS_BUTTON_A = By.xpath("//a[@class ='btn btn-success']");


    public static final By DELETE_XPATH = By.xpath("//button[contains(text(), 'Delete')]");
    public static final By DANGER_BUTTON = By.xpath("//button[@class='btn btn-danger']");

    public static final By I_INFO = By.xpath("//i[@class='fas fa-eye']");
    public static final By I_EDIT = By.xpath("//i[@class='fas fa-edit']");

    public static final By ERROR_UNKNOWN = By.xpath("//strong[text()='unknown error...']");
    public static final By ERROR_VALIDATION = By.xpath("//strong[text()='Error Validation']");
    public static final By SUCCESS = By.xpath("//strong[contains(text(), 'success')]");


    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";


}



