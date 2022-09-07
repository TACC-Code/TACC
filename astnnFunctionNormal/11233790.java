class BackupThread extends Thread {
    protected void loadClass(String name, InputStream fileStream) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream dataSink = new ByteArrayOutputStream();
        int read = -1;
        byte[] array = new byte[10240];
        read = fileStream.read(array);
        while (read != -1) {
            dataSink.write(array, 0, read);
            read = fileStream.read(array);
        }
        array = dataSink.toByteArray();
        Class classObject = null;
        if (domain == null) {
            classObject = this.defineClass(name, array, 0, read);
        } else {
            classObject = this.defineClass(name, array, 0, read, domain);
        }
        this.loadClass(name, true);
        loadMaps(classObject);
    }
}
