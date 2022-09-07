class BackupThread extends Thread {
    private void savePart(Part p, String fileName, boolean append, ProcessedMessage pMessage) throws FileNotFoundException, IOException, MessagingException {
        if (fileName != null) {
            FileOutputStream appendedFile = new FileOutputStream(pMessage.getFolderPath() + fileName, append);
            InputStream in = new BufferedInputStream(p.getInputStream());
            int b;
            while ((b = in.read()) != -1) appendedFile.write(b);
            appendedFile.flush();
            appendedFile.close();
            in.close();
            pMessage.getAttachments().add(fileName);
        }
    }
}
