class BackupThread extends Thread {
    public InputStream getReadAllInputStream() throws IOException {
        tailStream.flush();
        BufferedInputStream inStream1;
        if (inFile == null) {
            inStream1 = null;
        } else {
            FileInputStream tmpFileStream1 = new FileInputStream(inFile);
            tmpFileStream1.getChannel().position(headStream.getReadPosition());
            inStream1 = new BufferedInputStream(tmpFileStream1, 4096);
        }
        tailStream.flush();
        FileInputStream tmpFileStream2 = new FileInputStream(outFile);
        BufferedInputStream inStream2 = new BufferedInputStream(tmpFileStream2, 4096);
        ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
        new ObjectOutputStream(baOutStream);
        ByteArrayInputStream baInStream = new ByteArrayInputStream(baOutStream.toByteArray());
        return new SequenceInputStream((inStream1 == null ? (InputStream) baInStream : (InputStream) new SequenceInputStream(baInStream, inStream1)), inStream2);
    }
}
