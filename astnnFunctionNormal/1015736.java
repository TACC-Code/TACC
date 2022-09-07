class BackupThread extends Thread {
    public static void copyFile(String from, String to) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);
        writeFile(readFile(fromFile), toFile);
    }
}
