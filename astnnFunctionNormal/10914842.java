class BackupThread extends Thread {
        public void write(OutputStream out) throws IOException {
            String fileName = params.get(FILE);
            String cfileName = params.get(CONF_FILE_SHORT);
            String sOffset = params.get(OFFSET);
            String sLen = params.get(LEN);
            String compress = params.get(COMPRESSION);
            String sChecksum = params.get(CHECKSUM);
            String sindexVersion = params.get(CMD_INDEX_VERSION);
            if (sindexVersion != null) indexVersion = Long.parseLong(sindexVersion);
            if (Boolean.parseBoolean(compress)) {
                fos = new FastOutputStream(new DeflaterOutputStream(out));
            } else {
                fos = new FastOutputStream(out);
            }
            FileInputStream inputStream = null;
            int packetsWritten = 0;
            try {
                long offset = -1;
                int len = -1;
                boolean useChecksum = Boolean.parseBoolean(sChecksum);
                if (sOffset != null) offset = Long.parseLong(sOffset);
                if (sLen != null) len = Integer.parseInt(sLen);
                if (fileName == null && cfileName == null) {
                    writeNothing();
                }
                File file = null;
                if (cfileName != null) {
                    file = new File(core.getResourceLoader().getConfigDir(), cfileName);
                } else {
                    file = new File(core.getIndexDir(), fileName);
                }
                if (file.exists() && file.canRead()) {
                    inputStream = new FileInputStream(file);
                    FileChannel channel = inputStream.getChannel();
                    if (offset != -1) channel.position(offset);
                    byte[] buf = new byte[(len == -1 || len > PACKET_SZ) ? PACKET_SZ : len];
                    Checksum checksum = null;
                    if (useChecksum) checksum = new Adler32();
                    ByteBuffer bb = ByteBuffer.wrap(buf);
                    while (true) {
                        bb.clear();
                        long bytesRead = channel.read(bb);
                        if (bytesRead <= 0) {
                            writeNothing();
                            fos.close();
                            break;
                        }
                        fos.writeInt((int) bytesRead);
                        if (useChecksum) {
                            checksum.reset();
                            checksum.update(buf, 0, (int) bytesRead);
                            fos.writeLong(checksum.getValue());
                        }
                        fos.write(buf, 0, (int) bytesRead);
                        fos.flush();
                        if (indexVersion != null && (packetsWritten % 5 == 0)) {
                            delPolicy.setReserveDuration(indexVersion, reserveCommitDuration);
                        }
                        packetsWritten++;
                    }
                } else {
                    writeNothing();
                }
            } catch (IOException e) {
                LOG.warn("Exception while writing response for params: " + params, e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
}
