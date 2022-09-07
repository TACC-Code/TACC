class BackupThread extends Thread {
        public InputStream getStream() throws IOException {
            return url.openStream();
        }
}
