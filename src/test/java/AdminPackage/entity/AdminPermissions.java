package AdminPackage.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Helper.ActionsClass.getRandomBoolean;
import static SQL.AdminSQL.getPermissionsFromBD;

public class AdminPermissions {

    Access advertPrimaryInfoPermission = new Access();
    Access advertContactPermission = new Access();
    Access advertRequisitesPermission = new Access();
    Access advertPostbackPermission = new Access();
    Access advertDocumentPermission = new Access();
    Access advertNotePermission = new Access();

    ArrayList<Access> advertAccess = new ArrayList<>();

/*    Access[] advertAccess2 = {
            advertPrimaryInfoPermission,
            advertContactPermission,
            advertRequisitesPermission,
            advertPostbackPermission,
            advertDocumentPermission,
            advertNotePermission
    };

    String[] advertPermissions = {
            "advert_primary_info",
            "advert_contact",
            "advert_requisites",
            "advert_postback",
            "advert_document",
            "advert_note"};*/

    Map<String, Access> advertAccessMap = new HashMap<>();


    public AdminPermissions() throws Exception {
        advertAccessMap.put("advert_primary_info", advertPrimaryInfoPermission);
        advertAccessMap.put("advert_contact", advertContactPermission);
        advertAccessMap.put("advert_requisites", advertRequisitesPermission);
        advertAccessMap.put("advert_postback", advertPostbackPermission);
        advertAccessMap.put("advert_document", advertDocumentPermission);
        advertAccessMap.put("advert_note", advertNotePermission);

        for (Map.Entry<String, Access> entry : advertAccessMap.entrySet()) {
            List<String> permissionFromBD = getPermissionsFromBD(103, entry.getKey());
            System.out.println(permissionFromBD + " - " + entry.getKey());
            this.advertAccess.add(setPermissionBase(entry.getValue(), permissionFromBD));
        }
    }

    public Access setPermissionBase(Access basePermission, List<String> permissionFromBD) {
        basePermission.setView(permissionFromBD.contains("view"));
        basePermission.setCreate(permissionFromBD.contains("create"));
        basePermission.setEdit(permissionFromBD.contains("edit"));
        basePermission.setDelete(permissionFromBD.contains("delete"));
        basePermission.setExport(permissionFromBD.contains("export"));

        return basePermission;
    }

    public static class Access {
        Boolean view;
        Boolean create;
        Boolean edit;
        Boolean delete;
        Boolean export;

        public Access(boolean view, boolean create, boolean edit, boolean delete, boolean export) {
            this.view = view;
            this.create = create;
            this.edit = edit;
            this.delete = delete;
            this.export = export;
        }

        public Access() {
        }

        public Access(String random) {
            this.view = getRandomBoolean();
            this.create = getRandomBoolean();
            this.edit = getRandomBoolean();
            this.delete = getRandomBoolean();
            this.export = getRandomBoolean();
        }


        public Boolean getView() {
            return view;
        }

        public void setView(Boolean view) {
            this.view = view;
        }

        public Boolean getCreate() {
            return create;
        }

        public void setCreate(Boolean create) {
            this.create = create;
        }

        public Boolean getEdit() {
            return edit;
        }

        public void setEdit(Boolean edit) {
            this.edit = edit;
        }

        public Boolean getDelete() {
            return delete;
        }

        public void setDelete(Boolean delete) {
            this.delete = delete;
        }

        public Boolean getExport() {
            return export;
        }

        public void setExport(Boolean export) {
            this.export = export;
        }

    }

    public static class FullAccess extends Access {
        public FullAccess() {
            super(true, true, true, true, true);
        }
    }

    public static class ViewAccess extends Access {
        public ViewAccess() {
            super(true, false, false, false, false);
        }
    }

    public static class NullAccess extends Access {
        public NullAccess() {
            super(false, false, false, false, false);
        }
    }


    public Access getAdvertContactPermission() {
        return advertContactPermission;
    }

    public void setAdvertContactPermission(Access advertContactPermission) {
        this.advertContactPermission = advertContactPermission;
    }

    public Access getAdvertRequisitesPermission() {
        return advertRequisitesPermission;
    }

    public void setAdvertRequisitesPermission(Access advertRequisitesPermission) {
        this.advertRequisitesPermission = advertRequisitesPermission;
    }

    public Access getAdvertPostbackPermission() {
        return advertPostbackPermission;
    }

    public void setAdvertPostbackPermission(Access advertPostbackPermission) {
        this.advertPostbackPermission = advertPostbackPermission;
    }

    public Access getAdvertDocumentPermission() {
        return advertDocumentPermission;
    }

    public void setAdvertDocumentPermission(Access advertDocumentPermission) {
        this.advertDocumentPermission = advertDocumentPermission;
    }

    public Access getAdvertNotePermission() {
        return advertNotePermission;
    }

    public void setAdvertNotePermission(Access advertNotePermission) {
        this.advertNotePermission = advertNotePermission;
    }


//    private Map<AdminSection, Map<Action, Boolean>> accessMap;
//
//    public enum Action {
//        VIEW, CREATION, EDITING, DELETE, DATA_EXPORT
//    }
//
//    public enum AdminSection {
//        ADMIN,
//        ADMIN_LOG,
//        ADMIN_CONTENT_FILTER,
//        AUDIT_LOG
//    }
//
//    public enum AdvertSection {
//        ADVERT_PRIMARY_INFO,
//        ADVERT_CONTACT,
//        ADVERT_REQUISITES,
//        ADVERT_POSTBACK,
//        ADVERT_DOCUMENT,
//        ADVERT_NOTE
//    }
//
//    public enum AffiliateSection {
//        AFFILIATE, AFFILIATE_LOG
//    }
//
//    public enum OfferSection {
//        OFFER
//    }
//
//    public enum SettingSection {
//        SETTING,
//        SETTING_TAG,
//        SETTING_TRACKING_DOMAINS,
//        SETTING_USER_REQUEST_SOURCE
//    }
//
//    public enum Section {
//        SETTING,
//        SETTING_TAG,
//        SETTING_TRACKING_DOMAINS,
//        SETTING_USER_REQUEST_SOURCE
//    }
//
//    public AdminPermissions() {
//        accessMap = new EnumMap<>(AdminSection.class);
//        for (AdminSection section : AdminSection.values()) {
//            accessMap.put(section, new EnumMap<>(Action.class));
//        }
//        initializeAdminAccess();
//    }
//
//    private void initializeAdminAccess() {
//        //
//        setAccess(AdminSection.ADMIN, Action.VIEW, true);
//        setAccess(AdminSection.ADMIN, Action.CREATION, true);
//        setAccess(AdminSection.ADMIN, Action.EDITING, true);
//        setAccess(AdminSection.ADMIN, Action.DELETE, true);
//        setAccess(AdminSection.ADMIN, Action.DATA_EXPORT, true);
//        //
//        setAccess(AdminSection.ADMIN_LOG, Action.VIEW, true);
//        //
//        setAccess(AdminSection.ADMIN_CONTENT_FILTER, Action.VIEW, true);
//        setAccess(AdminSection.ADMIN_CONTENT_FILTER, Action.CREATION, true);
//        setAccess(AdminSection.ADMIN_CONTENT_FILTER, Action.EDITING, true);
//        setAccess(AdminSection.ADMIN_CONTENT_FILTER, Action.DELETE, true);
//        //
//    }
//
//    public void setAccess(AdminSection section, Action action, boolean isAllowed) {
//        accessMap.get(section).put(action, isAllowed);
//    }
//
//    public boolean hasAdminAccess(AdminSection section, Action action) {
//        return accessMap.get(section).getOrDefault(action, false);
//    }
//}


}