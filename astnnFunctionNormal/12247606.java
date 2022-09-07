class BackupThread extends Thread {
    public File createTmpFile(InputStream io) {
        try {
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "tmp.csv");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[256];
            int read = 0;
            while ((read = io.read(buf)) > 0) {
                fos.write(buf, 0, read);
            }
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File("");
    }
}
