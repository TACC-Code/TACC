class BackupThread extends Thread {
    public String readRecord(String filename) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        recordWriter.writeRecords(outputStream, recordReader.read(openInputFile(filename)));
        return outputStream.toString();
    }
}
