class BackupThread extends Thread {
    public static String readStringFromStream(InputStream stream, String charsetName) throws RegainException {
        InputStreamReader reader = null;
        try {
            if (charsetName == null) {
                reader = new InputStreamReader(stream);
            } else {
                reader = new InputStreamReader(stream, charsetName);
            }
            StringWriter writer = new StringWriter();
            RegainToolkit.pipe(reader, writer);
            reader.close();
            writer.close();
            return writer.toString();
        } catch (IOException exc) {
            throw new RegainException("Reading String from stream failed", exc);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException exc) {
                }
            }
        }
    }
}
