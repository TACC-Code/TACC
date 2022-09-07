class BackupThread extends Thread {
    public static void copyResource(File src, File dest) throws FileNotFoundException, IOException {
        Messages.writeToFile(dest, Messages.readFile(src));
    }
}
