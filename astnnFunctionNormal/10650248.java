class BackupThread extends Thread {
        protected void copyStream(InputStream content) throws IOException {
            out.flush();
            OutputStream dest = clientSocket.getOutputStream();
            byte[] buffer = new byte[4096];
            int numBytes;
            while ((numBytes = content.read(buffer)) != -1) dest.write(buffer, 0, numBytes);
            dest.flush();
            content.close();
        }
}
