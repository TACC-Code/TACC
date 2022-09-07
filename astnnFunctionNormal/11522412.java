class BackupThread extends Thread {
    private static boolean checkFtpDirec(File ftpDir) {
        if (!ftpDir.exists()) ; else if (!ftpDir.isDirectory()) ; else if (!ftpDir.canRead()) Util.writeLog("---Prompt ftp files.", ftpDir.getAbsolutePath(), "cannot read"); else return true;
        return false;
    }
}
