class BackupThread extends Thread {
    protected ByteArrayOutputStream pull_buffer() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fromServer.read(buffer, 0, buffer.length)) != -1) output.write(buffer, 0, bytesRead);
        closeConnection();
        return output;
    }
}
