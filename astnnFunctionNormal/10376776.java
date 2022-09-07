class BackupThread extends Thread {
    public static boolean changeFileEnc(File file, String oldEnc, String newEnc) {
        return writeToFile(file, readAsString(file, oldEnc), false, newEnc);
    }
}
