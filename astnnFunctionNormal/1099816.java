class BackupThread extends Thread {
        private void openConnection() throws IOException {
            if (urlConn == null) {
                urlConn = config.getURL().openConnection();
                Map headers = config.getHeaders();
                if (headers != null) {
                    for (Iterator i = headers.keySet().iterator(); i.hasNext(); ) {
                        String name = (String) i.next();
                        String value = (String) config.getHeaders().get(name);
                        urlConn.setRequestProperty(name, value);
                    }
                }
            }
        }
}
