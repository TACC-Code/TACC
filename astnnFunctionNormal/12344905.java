class BackupThread extends Thread {
        public URLConnection openConnection(URL url) throws IOException {
            return jEditResourceHandler.openConnection(url);
        }
}
