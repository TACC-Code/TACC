class BackupThread extends Thread {
    private static void getDirectoryPrompter(String valuePrefix, String textPrefix, StringBuffer xyz, File ftpDir) {
        if (!ftpDir.canRead()) Util.writeLog("---Prompt ftp directory.", ftpDir.getAbsolutePath(), "cannot read"); else {
            String[] files = ftpDir.list();
            for (int i = 0; i < files.length; i++) {
                File zyx = new File(ftpDir, files[i]);
                if (zyx.isDirectory()) {
                    xyz.append("<option value=\"").append(valuePrefix + files[i]).append("\">").append(textPrefix + files[i]);
                    getDirectoryPrompter(valuePrefix + files[i] + File.separatorChar, textPrefix + files[i] + File.separatorChar, xyz, zyx);
                }
            }
        }
    }
}
