class BackupThread extends Thread {
    protected static void writeToFile(InputStream instream, String fileName) throws IOException, XAwareException {
        try {
            Resource resource = ResourceHelper.getResource(fileName);
            if (resource != null) {
                File file = resource.getFile();
                java.io.OutputStream output1 = new java.io.FileOutputStream(file);
                if (output1 == null) {
                    String msg = "Unable to create output stream for :" + fileName;
                    logger.severe(msg, className, "writeToFile");
                    throw new XAwareException(msg);
                } else {
                    java.io.BufferedOutputStream outBuff = new java.io.BufferedOutputStream(output1);
                    int dataByte;
                    BufferedReader in = new BufferedReader(new InputStreamReader(instream));
                    while ((dataByte = in.read()) != -1) outBuff.write(dataByte);
                    outBuff.flush();
                    output1.close();
                }
            }
        } catch (IOException e) {
            logger.severe("HTTP IO exception" + e.getMessage(), className, "writeToFile");
            throw e;
        }
    }
}
