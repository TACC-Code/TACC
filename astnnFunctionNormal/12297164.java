class BackupThread extends Thread {
    protected void parse(URL url) {
        try {
            InputStream in = url.openStream();
            try {
                ZipInputStream zipStream = new ZipInputStream(in);
                for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
                    if (entry.getName().endsWith(CLASS_ENTRY_SUFFIX)) {
                        parse(getClass(entry.getName()));
                    }
                    zipStream.closeEntry();
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }
}
