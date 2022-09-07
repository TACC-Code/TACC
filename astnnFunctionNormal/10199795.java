class BackupThread extends Thread {
    public void write(final RandomAccessFile file) throws IOException, TagException {
        final File originalFile = getMp3file().getMp3file();
        final File newFile = new File(originalFile.getParentFile(), composeFilename());
        if (!newFile.getName().equals(originalFile.getName())) {
            file.getFD().sync();
            file.getChannel().close();
            file.close();
            TagUtility.copyFile(originalFile, newFile);
            if (!originalFile.delete()) {
                throw new TagException("Unable to delete original file: " + originalFile.getName());
            }
        }
    }
}
