class BackupThread extends Thread {
    public List<ErazeMethod> load(File file) throws FileNotFoundException, IOException {
        FileInputStream is = null;
        FileChannel channel = null;
        ByteBuffer buffer;
        List<ErazeMethod> methods = new ArrayList<ErazeMethod>(0);
        String header = null;
        int version = 0;
        long crc = 0l;
        try {
            is = new FileInputStream(file);
            channel = is.getChannel();
            buffer = readHeader(channel);
            header = getHeader(buffer);
            version = getVersion(buffer);
            crc = getCRC(buffer);
            verifyFormat(header, version);
            methods = new ArrayList<ErazeMethod>();
            readMethods(channel, methods);
            verifyCRC(crc, methods);
        } finally {
            closeInputStream(is);
        }
        return methods;
    }
}
