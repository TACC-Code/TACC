class BackupThread extends Thread {
    private void hardlinkOrCopy(File file, File destination) throws IOException {
        Process link = Runtime.getRuntime().exec("ln " + file.getAbsolutePath() + " " + destination.getAbsolutePath());
        try {
            link.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (link.exitValue() != 0) {
            FileUtils.copyFile(file, destination);
        }
    }
}
