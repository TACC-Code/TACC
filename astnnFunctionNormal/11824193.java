class BackupThread extends Thread {
    protected void gcrDataWriteback() {
        boolean isDirty = gcrDataDirty;
        gcrDataDirty = false;
        if (image == null || !isDirty) {
            return;
        }
        if (image.readOnly) {
            System.err.println("Attempt to write to read-only disk image.");
        } else {
            try {
                image.gcrDataWriteback(currentHalfTrack >> 1);
            } catch (final IOException e) {
                System.err.println(String.format("Error writing T:%d to disk image.", currentHalfTrack >> 1));
            }
        }
    }
}
