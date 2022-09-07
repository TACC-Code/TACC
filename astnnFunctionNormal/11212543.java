class BackupThread extends Thread {
    void copyFile(File sourceFile, File targetFile) throws IOException {
        RawTextReader reader = null;
        RawTextLogger writer = null;
        try {
            String sourceEncoding = det.getEncoding(sourceFile);
            reader = new RawTextReader(sourceFile, sourceEncoding);
            writer = new RawTextLogger(targetFile, true, true);
            while (run && reader.hasMoreElements()) {
                pause();
                writer.println(reader.nextElement());
            }
            writer.close();
            reader.close();
        } catch (IOException i) {
            MainGUI.LOGGER.error(i);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException i) {
                MainGUI.LOGGER.error(i);
            }
        }
    }
}
