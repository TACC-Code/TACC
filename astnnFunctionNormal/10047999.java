class BackupThread extends Thread {
    private File copyFileToScreenshotFolder(File file) {
        File dest = null;
        if (file != null) {
            try {
                dest = new File(path + "\\" + screenshotFolderName + "\\" + prefix + System.currentTimeMillis() + ".png");
                FileUtils.copyFile(file, dest);
                file.delete();
            } catch (Exception e) {
            }
        }
        return dest;
    }
}
