class BackupThread extends Thread {
    @Override
    protected List<ClassFetcher> find0(ExceptionHandler handler) {
        List<ClassFetcher> classes = new ArrayList<ClassFetcher>();
        try {
            JarInputStream jarStream = new JarInputStream(url.openStream());
            JarEntry entry;
            try {
                while ((entry = jarStream.getNextJarEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().endsWith(CLASS_FILE_EXTENSION)) {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        byte[] buffer = new byte[PACKET_SIZE];
                        int bytesRead;
                        while ((bytesRead = jarStream.read(buffer, 0, buffer.length)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                        byte[] classData = output.toByteArray();
                        classes.add(ClassFetcher.getFetcher(url, classData));
                    }
                }
            } finally {
                jarStream.close();
            }
        } catch (UnsupportedSourceException e) {
            handler.handle(e);
        } catch (IOException e) {
            handler.handle(e);
        }
        return classes;
    }
}
