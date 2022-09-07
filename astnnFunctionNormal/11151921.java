class BackupThread extends Thread {
    private String read(File inputFile) throws IOException {
        FileInputStream in = null;
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            in = new FileInputStream(inputFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) > 0) {
                str.write(bytes, 0, read);
            }
            return str.toString();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
