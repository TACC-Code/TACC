class BackupThread extends Thread {
    public RoleInfo(String roleName, String mbeanClassName, boolean read, boolean write) throws IllegalArgumentException, ClassNotFoundException, NotCompliantMBeanException {
        try {
            init(roleName, mbeanClassName, read, write, 1, 1, null);
        } catch (InvalidRoleInfoException exc) {
        }
        return;
    }
}
