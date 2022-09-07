class BackupThread extends Thread {
    private void unzipFile(InputStream inputStream, String directory) {
        try {
            Log.i(TAG, "Unzip the OSGi framework to: " + directory);
            JarInputStream jarInputStream = new JarInputStream(inputStream);
            JarEntry jarEntry;
            byte[] buffer = new byte[2048];
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                File jarEntryFile = new File(directory + File.separator + jarEntry.getName());
                if (jarEntry.isDirectory()) {
                    jarEntryFile.mkdirs();
                    continue;
                }
                FileOutputStream fos = new FileOutputStream(jarEntryFile);
                while (true) {
                    int read = jarInputStream.read(buffer);
                    if (read == -1) break;
                    fos.write(buffer, 0, read);
                }
                fos.close();
            }
            jarInputStream.close();
        } catch (Throwable t) {
            Log.w(TAG, "Error unzipping the zip file", t);
        }
    }
}
