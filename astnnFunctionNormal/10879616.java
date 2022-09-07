class BackupThread extends Thread {
    private void saveAttach(BodyPart part, String filePath) throws MessagingException, IOException {
        String srcFileName = part.getFileName();
        String decodeFileName = Base64.decode(srcFileName);
        if (logger.isDebugEnabled()) {
            logger.debug("saveAttach(BodyPart) - decodeFileName=" + decodeFileName + ", srcFileName=" + srcFileName + ", filePath=" + filePath);
        }
        InputStream in = part.getInputStream();
        FileOutputStream writer = new FileOutputStream(new File(getAccount().getCenterPath() + "/" + filePath));
        byte[] content = new byte[255];
        int read = 0;
        while ((read = in.read(content)) != -1) {
            writer.write(content, 0, read);
        }
        writer.close();
        in.close();
    }
}
