class BackupThread extends Thread {
    protected static void makeFile(File file, String shapePath) {
        try {
            file.createNewFile();
            BufferedOutputStream localFile = new BufferedOutputStream(new FileOutputStream(file));
            URL url = getJarFileURL(shapePath, file.getName());
            JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
            int fileSize = jarConnection.getContentLength();
            byte b[] = new byte[fileSize];
            BufferedInputStream jarFile = new BufferedInputStream(jarConnection.getInputStream());
            while (jarFile.read(b) != -1) {
                localFile.write(b);
            }
            localFile.close();
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
