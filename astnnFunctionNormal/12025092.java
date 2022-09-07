class BackupThread extends Thread {
    public static byte[] getBackImageData() throws OperationException {
        if (backgroundImageData != null) return backgroundImageData;
        try {
            String homePath = PathInfo.PROJECT_HOME;
            File imgFile = new File(homePath + backImageFileName);
            FileInputStream fileInStream = new FileInputStream(imgFile);
            ByteArrayOutputStream byteArrStream = new ByteArrayOutputStream();
            BufferedInputStream bufferedInStream = new BufferedInputStream(fileInStream);
            byte data[] = new byte[1024];
            int read = 0;
            while ((read = bufferedInStream.read(data)) != -1) {
                byteArrStream.write(data, 0, read);
            }
            byteArrStream.flush();
            backgroundImageData = byteArrStream.toByteArray();
            bufferedInStream.close();
            byteArrStream.close();
            fileInStream.close();
            return backgroundImageData;
        } catch (IOException ex) {
            throw new OperationException("Could not read backgroud image", ex);
        }
    }
}
