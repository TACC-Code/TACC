class BackupThread extends Thread {
    public static InputStream StringToPureInputStream(String argument) throws InvalidPluginArgumentException {
        File file = new File(argument);
        InputStream inputStream;
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new InvalidPluginArgumentException("Illegal attempt to convert a directory " + argument + " to an InputStream.");
            }
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                throw new InvalidPluginArgumentException("Exception attempting to convert existing file " + argument + " to an InputStream." + ex);
            }
        } else {
            try {
                URL url = new URL(argument);
                return url.openStream();
            } catch (Exception ex) {
                throw new InvalidPluginArgumentException("The arguement " + argument + " of type String, could not be converted " + "to either a URL or an existing file.");
            }
        }
    }
}
