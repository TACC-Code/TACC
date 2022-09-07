class BackupThread extends Thread {
    private byte[] loadFromArchive(ZipFile zip, String name) {
        ZipEntry entry;
        entry = zip.getEntry(name);
        if (entry == null) return null;
        ByteArrayOutputStream byteStream;
        InputStream stream;
        int count;
        try {
            stream = zip.getInputStream(entry);
            byteStream = new ByteArrayOutputStream((int) entry.getSize());
            byte[] buf = new byte[4096];
            while ((count = stream.read(buf)) > 0) byteStream.write(buf, 0, count);
            stream.close();
        } catch (IOException ioex) {
            return null;
        }
        return byteStream.toByteArray();
    }
}
