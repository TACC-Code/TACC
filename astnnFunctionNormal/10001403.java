class BackupThread extends Thread {
    private void doLoadFile(String name) {
        InputStream in;
        try {
            URL url = new URL(getDocumentBase(), name);
            in = url.openStream();
        } catch (Exception e) {
            canvas.setErrorMessage(null, "Unable to open file named \"" + name + "\": " + e);
            return;
        }
        Reader inputReader = new InputStreamReader(in);
        try {
            table.readFromStream(inputReader);
            inputReader.close();
        } catch (Exception e) {
            canvas.setErrorMessage(null, "Unable to get data from file \"" + name + "\": " + e.getMessage());
            return;
        }
        mainController.compute();
    }
}
