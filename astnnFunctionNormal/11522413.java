class BackupThread extends Thread {
    private static void getDirectoryFilePrompter(String valuePrefix, String textPrefix, StringBuffer xyz, File ftpDir) {
        if (!ftpDir.canRead()) Util.writeLog("---Prompt ftp files.", ftpDir.getAbsolutePath(), "cannot read"); else {
            String[] files = ftpDir.list();
            for (int i = 0; i < files.length; i++) {
                File zyx = new File(ftpDir, files[i]);
                if (zyx.isDirectory()) {
                    getDirectoryFilePrompter(valuePrefix + files[i] + File.separatorChar, textPrefix + files[i] + File.separatorChar, xyz, zyx);
                } else {
                    xyz.append("<option value=\"").append(valuePrefix + files[i]).append("\">").append(textPrefix + files[i]);
                }
            }
        }
    }
}
