class BackupThread extends Thread {
    public URLInputStream(String fileLocation) throws Exception {
        try {
            inputStream = new FileInputStream(fileLocation);
            url = new URL("file:" + fileLocation);
        } catch (FileNotFoundException e) {
            try {
                url = getClass().getClassLoader().getResource(fileLocation);
                inputStream = url.openStream();
            } catch (Exception e2) {
                url = new URL(fileLocation);
                inputStream = url.openStream();
            }
        }
    }
}
