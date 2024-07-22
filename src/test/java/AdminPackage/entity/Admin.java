package AdminPackage.entity;

import AdminPackage.entity.AdminPermissions.*;

public class Admin {
    public AdminGeneral adminGeneral;
    public AdminPermissions adminPermissions;
    int id;


    public Admin() throws Exception {
        this.adminGeneral = new AdminGeneral();
        this.adminPermissions = new AdminPermissions();
    }

    public AdminGeneral getAdminGeneral() {
        return adminGeneral;
    }

    public void setAdminGeneral(AdminGeneral adminGeneral) {
        this.adminGeneral = adminGeneral;
    }

    public AdminPermissions getAdminPermissions() {
        return adminPermissions;
    }

    public void setAdminPermissions(AdminPermissions adminPermissions) {
        this.adminPermissions = adminPermissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}