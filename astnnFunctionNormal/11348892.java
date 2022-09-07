class BackupThread extends Thread {
    protected byte[] readFileAsBytes(File file) throws IOException {
        final byte[] contents;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            if (bb.hasArray()) {
                contents = bb.array();
            } else {
                contents = new byte[sz];
                bb.get(contents);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to read file: " + file.getCanonicalPath(), e);
        } finally {
            if (fis != null) fis.close();
        }
        return contents;
    }
}
