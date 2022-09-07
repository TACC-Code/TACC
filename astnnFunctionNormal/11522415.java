class BackupThread extends Thread {
    private static void getDirectoryZipPrompter(String valuePrefix, String textPrefix, StringBuffer xyz, File ftpDir) {
        if (!ftpDir.canRead()) Util.writeLog("---Prompt ftp zipfiles.", ftpDir.getAbsolutePath(), "cannot read"); else {
            String[] files = ftpDir.list();
            for (int i = 0; i < files.length; i++) {
                File zyx = new File(ftpDir, files[i]);
                if (zyx.isDirectory()) {
                    getDirectoryZipPrompter(valuePrefix + files[i] + File.separatorChar, textPrefix + files[i] + File.separatorChar, xyz, zyx);
                } else {
                    int j = files[i].lastIndexOf('.');
                    if (j > 0) {
                        String ext = files[i].substring(j + 1);
                        if (ext.equals("zip") || ext.equals("ZIP")) xyz.append("<option value=\"").append(valuePrefix + files[i]).append("\">").append(textPrefix + files[i]);
                    }
                }
            }
        }
    }
}
