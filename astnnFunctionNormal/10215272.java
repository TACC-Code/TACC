class BackupThread extends Thread {
    private void throwPrintToFile() {
        SecurityManager security = System.getSecurityManager();
        FilePermission printToFilePermission = null;
        if (security != null) {
            if (printToFilePermission == null) {
                printToFilePermission = new FilePermission("<<ALL FILES>>", "read,write");
            }
            security.checkPermission(printToFilePermission);
        }
    }
}
