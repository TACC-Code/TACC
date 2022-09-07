class BackupThread extends Thread {
        private void outputToFile() {
            FileChannel destinationChannel = null;
            try {
                destinationChannel = new FileOutputStream(gobblerFileHandle).getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                byte[] bufferArray = buffer.array();
                while (true) {
                    buffer.clear();
                    int lim = inputStream.read(bufferArray);
                    if (lim == -1) break;
                    buffer.flip();
                    buffer.limit(lim);
                    while (buffer.hasRemaining()) {
                        destinationChannel.write(buffer);
                    }
                }
            } catch (IOException ioe) {
                LOGGER.error("IOException thrown when attempting to read InputStream from external process.", ioe);
                exceptionThrownByGobbler = ioe;
            } finally {
                try {
                    if (destinationChannel != null) {
                        destinationChannel.close();
                    }
                    inputStream.close();
                } catch (IOException ioe) {
                    LOGGER.error("IOException thrown when attempting to close " + "BufferedReader from external process.", ioe);
                    exceptionThrownByGobbler = ioe;
                }
            }
        }
}
