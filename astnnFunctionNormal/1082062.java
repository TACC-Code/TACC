class BackupThread extends Thread {
    private void checkInput() throws MalformedURLException, IOException {
        if (listFile != null) {
            uriLabel.setText("file" + listFile.getAbsolutePath());
            InputStream stream = new FileInputStream(listFile);
            checkInputStream(stream);
        } else if (uri != null) {
            uriLabel.setText(uri);
            URL url = new URL(uri);
            InputStream stream = url.openStream();
            checkInputStream(stream);
        } else {
            uriLabel.setText(NO_URI_OR_FILE);
            countLabel.setText(NO_URI_OR_FILE);
            ready = false;
        }
        parentGui.checkReady();
    }
}
