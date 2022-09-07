class BackupThread extends Thread {
    @SuppressWarnings("empty-statement")
    private byte[] addBinaryFile(File file) {
        if (file == null) throw new IllegalArgumentException("null fileName");
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            Log.out("File not found - '" + file.getAbsolutePath() + "'");
            return null;
        }
        FileChannel fileChannel = fileInputStream.getChannel();
        byte[] buffer = null;
        try {
            buffer = new byte[(int) fileChannel.size()];
        } catch (IOException exception) {
            Log.out("IOException reading file '" + file.getAbsolutePath() + "' - " + exception.getMessage());
            return null;
        }
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            while (fileChannel.read(byteBuffer) > 0) ;
            fileChannel.close();
        } catch (IOException exception) {
            Log.out("IOException reading file '" + file.getAbsolutePath() + "' - " + exception.getMessage());
            return null;
        }
        if (caching) files.put(file.getAbsolutePath(), buffer);
        Log.out("Loaded file '" + file.getAbsolutePath() + "'");
        return buffer;
    }
}
