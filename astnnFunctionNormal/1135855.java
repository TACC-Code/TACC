class BackupThread extends Thread {
    public Main(String projectName) {
        if (projectName != null) setProjectName(projectName);
        if (sSingleton == null) {
            sSingleton = this;
            SecurityManager sm = System.getSecurityManager();
            try {
                if (sm != null) sm.checkPermission(new AllPermission());
                hasAllPermission = true;
                hasFilePermission = true;
            } catch (SecurityException ex) {
                try {
                    sm.checkPermission(new PropertyPermission("user.home", "read"));
                    sm.checkPermission(new FilePermission(new File(new File(System.getProperty("user.home")), "*").getAbsolutePath(), "read,write"));
                    hasFilePermission = true;
                } catch (SecurityException ex2) {
                }
            }
            loadProperties();
            loadData();
        }
    }
}
