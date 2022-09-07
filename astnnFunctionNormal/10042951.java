class BackupThread extends Thread {
        public void write() throws IOException, TarMalformatException {
            int i;
            try {
                writeField(TarHeaderField.name, path);
                writeField(TarHeaderField.mode, fileMode);
                if (!paxSized) {
                    writeField(TarHeaderField.size, dataSize);
                }
                writeField(TarHeaderField.mtime, modTime);
                writeField(TarHeaderField.checksum, TarEntrySupplicant.prePaddedOctalString(headerChecksum(), 6) + "\0 ");
                tarStream.writeBlock(rawHeader);
                long dataStart = tarStream.getBytesWritten();
                while ((i = inputStream.read(tarStream.writeBuffer)) > 0) {
                    tarStream.write(i);
                }
                if (dataStart + dataSize != tarStream.getBytesWritten()) {
                    throw new IOException(RB.data_changed.getString(Long.toString(dataSize), Long.toString(tarStream.getBytesWritten() - dataStart)));
                }
                tarStream.padCurrentBlock();
            } finally {
                close();
            }
        }
}
