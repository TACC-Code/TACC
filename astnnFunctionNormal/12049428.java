class BackupThread extends Thread {
        private String readSourceFile(final File file) throws IOException {
            if (file == null) {
                throw new NullPointerException("file");
            }
            RandomAccessFile randomAccessFile = null;
            FileChannel fileChannel = null;
            FileLock fileLock = null;
            boolean suppressExceptionOnClose = true;
            final int length = file.length() > 0L ? Long.valueOf(file.length()).intValue() : 1;
            final ByteBuffer buf = ByteBuffer.allocate(length);
            final StringBuilder appendable = new StringBuilder(length);
            try {
                randomAccessFile = new RandomAccessFile(file, "r");
                fileChannel = randomAccessFile.getChannel();
                fileLock = fileChannel.lock(0L, file.length(), true);
                fileChannel.position(0L);
                buf.clear();
                int read = fileChannel.read(buf);
                while (read != -1) {
                    appendable.append(new String(buf.array(), buf.arrayOffset(), read, getInputEncoding()));
                    buf.clear();
                    read = fileChannel.read(buf);
                }
                suppressExceptionOnClose = false;
                return appendable.toString();
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
