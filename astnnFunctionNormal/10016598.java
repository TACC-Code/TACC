class BackupThread extends Thread {
    public static OutputStream openFileForWriting(File file, boolean allowOverwrite) {
        OutputStream osx = null;
        if (file.exists() && !allowOverwrite) throw new PngjOutputException("File already exists (" + file + ") and overwrite=false");
        try {
            osx = new FileOutputStream(file);
        } catch (Exception e) {
            throw new PngjOutputException("error opening " + file + " for writing", e);
        }
        return osx;
    }
}
