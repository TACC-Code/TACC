class BackupThread extends Thread {
    public void sendFile(File localFile, String remotePath, String remoteName, String token) throws Exception {
        if (localFile == null) {
            throw new IllegalArgumentException("null file");
        }
        if (!localFile.exists()) {
            throw new IllegalArgumentException(localFile.getAbsolutePath() + " does not exist");
        }
        this.filesSyncUpload++;
        String data = URLEncoder.encode("rp", "UTF-8") + "=" + URLEncoder.encode(remotePath, "UTF-8");
        data += "&" + URLEncoder.encode("rn", "UTF-8") + "=" + URLEncoder.encode(remoteName, "UTF-8");
        if (token != null) {
            data += "&" + URLEncoder.encode("tk", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
        }
        this.byteSent += data.getBytes().length;
        URLConnection conn = getConnection(this.baseUrl + "/getHash");
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();
        byte[] localFileHash = null;
        int localFileSize = (int) localFile.length();
        RangeList rangesOk = new RangeList(localFileSize);
        MoveOperationList moves = new MoveOperationList();
        this.byteSyncUpload = localFileSize;
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(conn.getInputStream()));
            int remoteFileSize = in.readInt();
            this.byteReceived += 4;
            final float a = (float) remoteFileSize / HashWriter.blockSize;
            int nb = (int) Math.ceil(a);
            HashMap<Integer, byte[]> map = new HashMap<Integer, byte[]>(nb * 2 + 1);
            HashMap<Integer, Integer> mapBlock = new HashMap<Integer, Integer>(nb * 2 + 1);
            for (int i = 0; i < nb; i++) {
                byte[] b = new byte[16];
                final int r32 = in.readInt();
                in.read(b);
                this.byteReceived += 4 + 16;
                map.put(r32, b);
                mapBlock.put(r32, i);
            }
            byte[] remoteFileHash = new byte[32];
            in.read(remoteFileHash);
            this.byteReceived += 32;
            in.close();
            if (localFileSize == remoteFileSize) {
                localFileHash = HashWriter.getHash(localFile);
                if (HashWriter.compareHash(localFileHash, remoteFileHash)) {
                    return;
                }
            }
            RollingChecksum32 checksum = new RollingChecksum32();
            byte[] buffer = new byte[HashWriter.blockSize];
            BufferedInputStream fb = new BufferedInputStream(new FileInputStream(localFile));
            final int read = fb.read(buffer);
            this.byteReceived += read;
            checksum.check(buffer, 0, read);
            int v = 0;
            int start = 0;
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            do {
                int r32 = checksum.getValue();
                byte[] md5 = map.get(r32);
                if (md5 != null) {
                    md5Digest.reset();
                    md5Digest.update(buffer);
                    byte[] localMd5 = md5Digest.digest();
                    if (HashWriter.compareHash(md5, localMd5)) {
                        int offset = mapBlock.get(r32) * HashWriter.blockSize;
                        MoveOperation m = new MoveOperation(offset, start, HashWriter.blockSize);
                        moves.add(m);
                        rangesOk.add(new Range(start, start + HashWriter.blockSize));
                    }
                }
                v = fb.read();
                start++;
                System.arraycopy(buffer, 1, buffer, 0, buffer.length - 1);
                buffer[buffer.length - 1] = (byte) v;
                checksum.roll((byte) v);
            } while (v >= 0);
            fb.close();
        } catch (FileNotFoundException e) {
        }
        if (localFileHash == null) {
            localFileHash = HashWriter.getHash(localFile);
        }
        sendDelta(localFile, remotePath, remoteName, moves, rangesOk.getUnusedRanges(), localFileHash, token);
    }
}
