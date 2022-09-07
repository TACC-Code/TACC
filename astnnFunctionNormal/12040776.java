class BackupThread extends Thread {
    private void writeMessageHeader(String fileName, ProcessedMessage pMessage) throws FileNotFoundException, IOException {
        File f = new File(pMessage.getFolderPath() + fileName);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        InputStream in = new ByteArrayInputStream(pMessage.getMessageHeader().getBytes());
        int b;
        while ((b = in.read()) != -1) out.write(b);
        out.flush();
        out.close();
        in.close();
    }
}
