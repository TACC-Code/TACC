class BackupThread extends Thread {
    private void copyJarFile(File jarFile) {
        File destFile = new File(layout.getLibDir(), "protoj.jar");
        FileUtils.copyFile(jarFile, destFile);
    }
}
