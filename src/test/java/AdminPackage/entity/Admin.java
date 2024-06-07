package AdminPackage.entity;

import AdminPackage.entity.AdminPermissions.*;
public class Admin {
    int id;

    Access adminPermission;
    Access adminLogPermission ;
    Access dashboardPermission = new ViewAccess();
    Access offersPermission = new FullAccess();
    Access advertisersPermission = new FullAccess();
    Access advertisersDocumentsPermission = new FullAccess();
// TODO отправлять API на разные варианты доступов чекбоксами фиг проставишь

    public Admin() {
        this.adminPermission = new FullAccess();
        this.adminLogPermission = new ViewAccess();
    }
}