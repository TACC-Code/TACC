class BackupThread extends Thread {
                        public InputStream run() throws Exception {
                            try {
                                return url.openStream();
                            } catch (IOException e) {
                                LOG.warn("URL canot be accessed: " + e.getMessage());
                            }
                            return null;
                        }
}
