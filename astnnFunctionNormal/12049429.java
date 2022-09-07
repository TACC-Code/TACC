class BackupThread extends Thread {
        private void writeSourceFile(final File file, final String content) throws IOException {
            if (file == null) {
                throw new NullPointerException("file");
            }
            if (content == null) {
                throw new NullPointerException("content");
            }
            RandomAccessFile randomAccessFile = null;
            FileChannel fileChannel = null;
            FileLock fileLock = null;
            boolean suppressExceptionOnClose = true;
            final byte[] bytes = content.getBytes(getOutputEncoding());
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                fileChannel = randomAccessFile.getChannel();
                fileLock = fileChannel.lock(0L, bytes.length, false);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
                suppressExceptionOnClose = false;
            } finally {
                try {
                    if (fileLock != null) {
                        fileLock.release();
                    }
                } catch (final IOException e) {
                    if (suppressExceptionOnClose) {
                        log(Level.SEVERE, null, e);
                    } else {
                        throw e;
                    }
                } finally {
                    try {
                        if (fileChannel != null) {
                            fileChannel.close();
                        }
                    } catch (final IOException e) {
                        if (suppressExceptionOnClose) {
                            log(Level.SEVERE, null, e);
                        } else {
                            throw e;
                        }
                    } finally {
                        try {
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                        } catch (final IOException e) {
                            if (suppressExceptionOnClose) {
                                log(Level.SEVERE, null, e);
                            } else {
                                throw e;
                            }
                        }
                    }
                }
            }
        }
}
