class BackupThread extends Thread {
    private void mountService(ServiceDefinition servDef, String a_username, String a_password, Vector usrgrp) {
        iFileService ifs = new iFileService(servDef.className, servDef.name, servDef.description, a_username == null ? servDef.userName : a_username, a_password == null ? servDef.password : a_password, servDef.path, servDef.connectionstring, servDef.adminGroup, servDef.icon);
        Iterator it = servDef.permissions.values().iterator();
        boolean valid = false;
        while (it.hasNext()) {
            ServicePermissions servPermissions = (ServicePermissions) it.next();
            ifs.setPermissions(servPermissions.group, servPermissions.browse, servPermissions.read, servPermissions.write, servPermissions.createnew, servPermissions.delete, servPermissions.versioncontrol);
            for (int k = 0; usrgrp != null && k < usrgrp.size(); k++) {
                if ((String) usrgrp.get(k) != null && ((String) usrgrp.get(k)).equalsIgnoreCase(servPermissions.group)) valid = true;
            }
            if ((servPermissions.group.equals("*") || servPermissions.group.equals("public")) && valid == false) {
                valid = true;
            }
        }
        if (valid) {
            ifs.open();
            p_ifs.put(servDef.name, ifs);
        }
    }
}
