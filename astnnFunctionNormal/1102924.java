class BackupThread extends Thread {
    public static boolean copyFileTo(File src, File dest) {
        try {
            org.apache.commons.io.FileUtils.copyFile(src, dest);
            String size = org.apache.commons.io.FileUtils.byteCountToDisplaySize(dest.length());
            log.info("Copied " + size + " to: " + dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dest.exists() && dest.length() == src.length()) {
            return true;
        } else {
            return false;
        }
    }
}
