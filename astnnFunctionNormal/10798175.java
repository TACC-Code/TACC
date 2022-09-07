class BackupThread extends Thread {
    public static int askExistingFile(File file, Component c) {
        return existFile(file) ? showYesNoCancelDialog("File " + file.getName() + " already exists: overwrite it?", "Warning", c, true) : OK_PRESSED;
    }
}
