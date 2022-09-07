class BackupThread extends Thread {
    private static void writeBinary(String filename, OutputStream output) throws IOException {
        FileInputStream inputStream = new FileInputStream(filename);
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer)) > -1) output.write(buffer, 0, bytesRead);
        inputStream.close();
    }
}
