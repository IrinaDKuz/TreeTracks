package AdminPackage.entity;

import static Helper.ActionsClass.getRandomBoolean;

public class AdminPermissions {

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
            this.view = getRandomBoolean();
            this.create = getRandomBoolean();
            this.edit = getRandomBoolean();
            this.delete = getRandomBoolean();
            this.export = getRandomBoolean();
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
}