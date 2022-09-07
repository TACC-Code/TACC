class BackupThread extends Thread {
    private static void install(final String inputPath, final String file, final String outputPath) throws FileNotFoundException, IOException {
        if (new File(outputPath, file).isFile()) {
            logger.fine(file + " is allready installed.");
            return;
        }
        final String resourcePath = inputPath.toLowerCase() + "/" + file;
        final InputStream is = BinariesInstaller.class.getResourceAsStream(resourcePath);
        if (is == null) throw new FileNotFoundException("Unable to find the correct resource (" + resourcePath + ")");
        final File outputDir = new File(outputPath);
        if (!outputDir.isDirectory()) if (!outputDir.mkdirs()) throw new IOException("Can't create directory for binaries installation: " + outputDir.getAbsolutePath());
        final File outputFile = new File(outputDir, file);
        OutputStream fos = new FileOutputStream(outputFile);
        byte[] buf = new byte[BUFFER_SIZE];
        int i = 0;
        while ((i = is.read(buf)) != -1) fos.write(buf, 0, i);
        is.close();
        fos.close();
        FileUtils.setExecutable(outputFile, false);
        FileUtils.setReadable(outputFile, false);
    }
}
