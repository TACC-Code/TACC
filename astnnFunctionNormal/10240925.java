class BackupThread extends Thread {
        @Override
        protected InputStream getScriptInputStream() {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new UnitilsException("Error while trying to create reader for url " + url, e);
            }
        }
}
