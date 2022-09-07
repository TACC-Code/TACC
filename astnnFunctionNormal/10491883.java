class BackupThread extends Thread {
    public PermissionCollection getPermissions() {
        Permissions permissions = new Permissions();
        if (type == ALL_PERMISSIONS) {
            permissions.add(new AllPermission());
            return permissions;
        }
        if (type == SANDBOX_PERMISSIONS) {
            for (int i = 0; i < sandboxPermissions.length; i++) permissions.add(sandboxPermissions[i]);
            if (downloadHost != null) permissions.add(new SocketPermission(downloadHost, "connect, accept"));
        }
        if (type == J2EE_PERMISSIONS) for (int i = 0; i < j2eePermissions.length; i++) permissions.add(j2eePermissions[i]);
        PropertyDesc props[] = file.getResources().getProperties();
        for (int i = 0; i < props.length; i++) {
            permissions.add(new PropertyPermission(props[i].getKey(), "read,write"));
        }
        return permissions;
    }
}
