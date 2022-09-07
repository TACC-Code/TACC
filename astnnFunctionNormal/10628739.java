class BackupThread extends Thread {
    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        InputStream input = null;
        try {
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            if (is instanceof BufferedInputStream) input = (BufferedInputStream) is; else input = new BufferedInputStream(is);
        } catch (IOException ioe) {
            throw new FileNotFoundException(ioe.getMessage());
        }
        return loadInternal(input);
    }
}
