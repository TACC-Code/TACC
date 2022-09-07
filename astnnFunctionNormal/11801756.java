class BackupThread extends Thread {
    private void processFile(File file) throws Exception {
        ZipFile jf = new ZipFile(file);
        Enumeration en = jf.entries();
        while (en.hasMoreElements()) {
            ZipEntry jarEntry = (ZipEntry) en.nextElement();
            notifyEvent(new EntryJarEvent(jarEntry.getName()));
            if (jarEntry.getName().endsWith("jar")) {
                String timeStamp = System.currentTimeMillis() + "";
                File nestedJarFile = new File(timeStamp + file.getName());
                System.out.println(jarEntry.getName());
                InputStream is = jf.getInputStream(jarEntry);
                FileOutputStream fos = new java.io.FileOutputStream(nestedJarFile);
                while (is.available() > 0) {
                    fos.write(is.read());
                }
                fos.close();
                is.close();
                notifyEvent(new StartNestedJarEvent(jarEntry.getName()));
                processFile(nestedJarFile);
                nestedJarFile.delete();
                notifyEvent(new EndNestedJarEvent(jarEntry.getName()));
            }
        }
    }
}
