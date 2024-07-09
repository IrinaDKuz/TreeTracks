package AdminPackage.entity;

import static Helper.ActionsClass.generateRandomString;
import static Helper.Admin.*;
import static Helper.Adverts.generateName;
import static Helper.GeoAndLang.*;
import static Helper.Settings.generateRandomNumber;
import static SQL.AdvertSQL.getRandomValueFromBD;

public class AdminGeneral {
    String status;
    String roleId;
    String roleName;
    String email;
    String password;
    String firstName;
    String lastName;

    String skype;
    String telegram;
    String phone;
    String workingHours;


    public AdminGeneral() {
    }

    public void fillAdminGeneralWithRandomDataForAPI() throws Exception {
        this.status = getRandomKey(ADMIN_STATUS_MAP);
        this.roleId = getRandomValueFromBD("id", "role");
        this.firstName = generateName(2, ANIMAL_WORDS);
        this.lastName = generateName(2, ANIMAL_WORDS);
        this.email = generateEmail(firstName + lastName);
        this.password = generateRandomString(10);
        this.skype = "Skype:" + firstName;
        this.telegram = "@Telegram" + lastName;
        this.phone = String.valueOf(generateRandomNumber(10));
        this.workingHours = String.valueOf(generateRandomNumber(2));
    }

    public void fillAdvertPrimaryInfoWithRandomDataForUI() throws Exception {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
