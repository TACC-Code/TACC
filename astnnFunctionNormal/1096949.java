class BackupThread extends Thread {
    private static void installFile(InputStream in, String instPath, String fileName, boolean update) {
        instPath = instPath.replaceAll("/", File.separator);
        String path = GlobalProps.getHomeDir() + File.separator + instPath;
        try {
            File instDir = new File(path);
            if (!instDir.isDirectory()) {
                instDir.mkdirs();
            }
            byte[] buf = new byte[1024];
            File instFile = new File(path + File.separator + fileName);
            if (update && instFile.exists()) {
                instFile.delete();
            }
            FileOutputStream fileOut = new FileOutputStream(instFile);
            int read = 0;
            while ((read = in.read(buf)) != -1) {
                fileOut.write(buf, 0, read);
            }
            in.close();
            fileOut.close();
        } catch (IOException ioe) {
            if (GlobalProps.DEBUG) {
                ioe.printStackTrace();
            }
        }
    }
}
