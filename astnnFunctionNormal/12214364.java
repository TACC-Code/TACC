class BackupThread extends Thread {
    public void publish(Payload payload) throws ResourceException {
        logger.info("publishing payload [" + payload.getId() + "] with name [" + payload.getAttribute(FilePayloadAttributeNames.FILE_NAME) + "]");
        if (isOutputPathADirectory() == false) {
            logger.warn("Output path [" + this.outputPath + "] is not a valid directory.");
            throw new ResourceException("Output path [" + this.outputPath + "] is not a valid directory");
        }
        byte[] payloadContent = payload.getContent();
        ByteBuffer inByteBuffer = ByteBuffer.wrap(payloadContent);
        String fileName = payload.getAttribute(FilePayloadAttributeNames.FILE_NAME);
        FileOutputStream fos = null;
        FileChannel ofc = null;
        try {
            File outputFile = new File(this.outputPath);
            logger.info("Publishing to [" + outputFile.getAbsolutePath() + "]");
            fos = new FileOutputStream(outputFile.getAbsolutePath() + File.separator + fileName);
            ofc = fos.getChannel();
            ofc.write(inByteBuffer);
            ofc.force(false);
        } catch (IOException e) {
            throw new ResourceException(e);
        } finally {
            try {
                if (ofc != null) {
                    ofc.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                throw new ResourceException(ioe);
            }
        }
    }
}
