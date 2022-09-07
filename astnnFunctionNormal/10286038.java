class BackupThread extends Thread {
    private static byte[] readClassData(File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            while ((read = fis.read(buffer)) > 0) {
                baos.write(buffer, 0, read);
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                }
            }
        }
        return baos.toByteArray();
    }
}
