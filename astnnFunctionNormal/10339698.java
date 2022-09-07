class BackupThread extends Thread {
    private static void copyFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            InputStream in = StandardFiles.class.getResourceAsStream("/de/blitzcoder/collide/standardfiles/" + fileName);
            while (in.available() != 0) {
                out.write(in.read());
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
