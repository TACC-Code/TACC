class BackupThread extends Thread {
    public void write(BufferedImage resource, OutputStream output) throws ResourceException {
        try {
            String[] formatNames = ImageIO.getWriterFormatNames();
            for (Iterator<ImageReader> readers = ImageIO.getImageReaders(resource); readers.hasNext(); ) {
                ImageWriter writer = ImageIO.getImageWriter(readers.next());
                if (writer != null) {
                    for (String formatName : formatNames) {
                        for (Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName); writers.hasNext(); ) {
                            if (writer.equals(writers.next())) {
                                ImageIO.write(resource, formatName, output);
                                return;
                            }
                        }
                    }
                }
            }
            throw new ResourceException("Unable to find suitable ImageWriter");
        } catch (ClassCastException e) {
            throw new ResourceException(e);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
}
