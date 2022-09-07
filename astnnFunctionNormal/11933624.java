class BackupThread extends Thread {
    public static void moveResource(File src, File dest) throws FileNotFoundException, IOException {
        Messages.writeToFile(dest, Messages.readFile(src));
        src.delete();
    }
}
