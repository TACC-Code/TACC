class BackupThread extends Thread {
    public static void copyFile(String resFolder, File outFolder, String fname, boolean replace) throws IOException {
        File f = new File(outFolder, fname);
        if (f.exists() && !f.canWrite()) {
            return;
        }
        if (f.exists() && !replace) {
            return;
        }
        FileOutputStream fos = new FileOutputStream(f);
        InputStream is = GameCompiler.class.getResourceAsStream(resFolder + fname);
        if (is == null) {
            throw new IOException("Resource " + resFolder + fname + " does not exist!");
        }
        int read;
        byte[] buffer = new byte[8192];
        while ((read = is.read(buffer)) != -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
        is.close();
    }
}
