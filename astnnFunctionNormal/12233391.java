class BackupThread extends Thread {
    private String fetchInterproNameFromInterproNameHandler(String id) {
        try {
            if (interproHandler == null) {
                InputStream stream = null;
                try {
                    URL url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/interpro/entry.list");
                    stream = url.openStream();
                    interproHandler = new InterproNameHandler(stream);
                } catch (IOException e) {
                    interproHandler = new InterproNameHandler(getFileByResources("/interpro-entry-local.txt", InterproNameHandler.class));
                }
            }
            return interproHandler.getNameById(id);
        } catch (NameNotFoundException e) {
            logger.info("No Description for " + id + " found.");
            return null;
        }
    }
}
