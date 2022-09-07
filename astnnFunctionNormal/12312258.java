class BackupThread extends Thread {
    public RoleInfo(String roleName, String mbeanClassName, boolean read, boolean write, int min, int max, String descr) throws IllegalArgumentException, InvalidRoleInfoException, ClassNotFoundException, NotCompliantMBeanException {
        init(roleName, mbeanClassName, read, write, min, max, descr);
        return;
    }
}
