class BackupThread extends Thread {
    public ByteArrayDataSource(InputStream is, String type) {
        _type = type;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) os.write(ch);
            _data = os.toByteArray();
        } catch (IOException ioe) {
        }
    }
}
