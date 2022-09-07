class BackupThread extends Thread {
        public TarEntrySupplicant(String path, InputStream origStream, int maxBytes, char typeFlag, TarFileOutputStream tarStream) throws IOException, TarMalformatException {
            this(path, typeFlag, tarStream);
            if (maxBytes < 1) {
                throw new IllegalArgumentException(RB.read_lt_1.getString());
            }
            int i;
            PipedOutputStream outPipe = new PipedOutputStream();
            try {
                inputStream = new PipedInputStream(outPipe);
                while ((i = origStream.read(tarStream.writeBuffer, 0, tarStream.writeBuffer.length)) > 0) {
                    outPipe.write(tarStream.writeBuffer, 0, i);
                }
                outPipe.flush();
                dataSize = inputStream.available();
                if (TarFileOutputStream.debug) {
                    System.out.println(RB.stream_buffer_report.getString(Long.toString(dataSize)));
                }
            } catch (IOException ioe) {
                close();
                throw ioe;
            } finally {
                try {
                    outPipe.close();
                } finally {
                    outPipe = null;
                }
            }
            modTime = new java.util.Date().getTime() / 1000L;
        }
}
