class BackupThread extends Thread {
    private InputStream openInputStream() throws IOException, FileNotFoundException {
        if (urlFile != null) return urlFile.openStream();
        if (inputFile != null) return new FileInputStream(inputFile);
        return null;
    }
}
