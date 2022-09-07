class BackupThread extends Thread {
    public static final byte[] unZipBuffer(byte[] buffer) {
        if (buffer == null) {
            throw new RuntimeException("buffer is null, unZipBuffer");
        }
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        ByteArrayOutputStream os = new ByteArrayOutputStream(1000);
        try {
            MLUtil.d("unZipBuffer: " + buffer.length);
            ZipInputStream zip = new ZipInputStream(is);
            ZipEntry zipentry = zip.getNextEntry();
            if (zipentry == null) {
                MLUtil.runtimeError(null, "unZipBuffer failure");
                return (null);
            }
            int n;
            byte[] temp = new byte[TEMP_FILE_BUFFER_SIZE];
            while ((n = zip.read(temp)) > -1) os.write(temp, 0, n);
            zip.closeEntry();
            zip.close();
            is.close();
            os.close();
            byte[] bb = os.toByteArray();
            MLUtil.d("uncompressed=" + bb.length);
            return (bb);
        } catch (IOException e) {
            MLUtil.runtimeError(e, "unzipBuffer");
        }
        return (null);
    }
}
