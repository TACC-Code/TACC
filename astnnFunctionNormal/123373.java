class BackupThread extends Thread {
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        in.close();
        out.close();
    }
}
