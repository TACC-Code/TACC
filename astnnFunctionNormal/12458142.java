class BackupThread extends Thread {
    private boolean uploadFile(FormFile formFile, String tempPath) throws FileNotFoundException, IOException {
        boolean success = false;
        if (formFile != null) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = formFile.getInputStream();
                outputStream = new FileOutputStream(tempPath);
                int bufferSize = 2048;
                byte[] bufferByte = new byte[bufferSize];
                int read = 0;
                while ((read = inputStream.read(bufferByte, 0, bufferSize)) >= 0) {
                    outputStream.write(bufferByte, 0, read);
                }
                success = true;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            }
        }
        return success;
    }
}
