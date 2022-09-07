class BackupThread extends Thread {
    public void write(File dest, InputStream in) {
        FileChannel channel = null;
        try {
            channel = new FileOutputStream(dest).getChannel();
        } catch (FileNotFoundException e) {
            logger.error("File '{}' is a directory or can not be open.", dest.getAbsolutePath());
            throw new ApplicationException(e);
        }
        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
                channel.write(byteBuffer);
                byteCount += bytesRead;
            }
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
