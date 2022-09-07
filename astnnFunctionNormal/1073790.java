class BackupThread extends Thread {
    private InputStream shiftInputStream(InputStream input) throws DropboxException {
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        int bufLen = 4096;
        byte[] buf = new byte[bufLen];
        int read = 0;
        try {
            while ((read = input.read(buf, 0, bufLen)) != -1) {
                byteArrayOutput.write(buf, 0, read);
            }
        } catch (IOException e) {
            throw new DropboxException(e);
        }
        ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(byteArrayOutput.toByteArray());
        return byteArrayInput;
    }
}
