class BackupThread extends Thread {
    public void copyFile(MultipartFile file, String target) throws IOException {
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = new FileOutputStream(target);
        int readBytes = 0;
        byte[] buffer = new byte[10000];
        while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
            outputStream.write(buffer, 0, readBytes);
        }
        outputStream.close();
        inputStream.close();
    }
}
