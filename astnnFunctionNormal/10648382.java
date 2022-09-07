class BackupThread extends Thread {
    public static void write2File(final Reader reader, final Writer writer, final boolean closeStream) {
        int byt;
        try {
            while ((byt = reader.read()) != -1) {
                writer.write(byt);
            }
            if (closeStream) {
                reader.close();
                writer.close();
            }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "write2File failed...\n" + e.getMessage(), e);
        } finally {
            if (closeStream) {
                StreamUtils.closeReader(reader);
                StreamUtils.closeWriter(writer);
            }
        }
    }
}
