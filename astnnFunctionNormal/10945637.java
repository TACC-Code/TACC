class BackupThread extends Thread {
    private void retrieveFileWithDelta(File localFile, String remotePath, String remoteName, String token) throws Exception {
        String data = URLEncoder.encode("rp", "UTF-8") + "=" + URLEncoder.encode(remotePath, "UTF-8");
        data += "&" + URLEncoder.encode("rn", "UTF-8") + "=" + URLEncoder.encode(remoteName, "UTF-8");
        if (token != null) {
            data += "&tk=" + URLEncoder.encode(token, "UTF-8");
        }
        this.byteSent += data.getBytes().length;
        URLConnection conn = getConnection(this.baseUrl + "/getHash");
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();
        DataInputStream in = new DataInputStream(new BufferedInputStream(conn.getInputStream()));
        int fileSize = in.readInt();
        this.byteReceived += 4;
        this.byteSyncDownload = fileSize;
        final float a = (float) fileSize / HashWriter.blockSize;
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
        byte[] fileHash = new byte[32];
        in.read(fileHash);
        this.byteReceived += 32;
        in.close();
        File newFile = createEmptyFile(localFile.getParentFile(), fileSize);
        RandomAccessFile rNewFile = new RandomAccessFile(newFile, "rw");
        RollingChecksum32 checksum = new RollingChecksum32();
        byte[] buffer = new byte[HashWriter.blockSize];
        BufferedInputStream fb = new BufferedInputStream(new FileInputStream(localFile));
        final int read = fb.read(buffer);
        checksum.check(buffer, 0, read);
        int v = 0;
        int start = 0;
        int end = read;
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        RangeList rangesOk = new RangeList(fileSize);
        do {
            int r32 = checksum.getValue();
            byte[] md5 = map.get(r32);
            if (md5 != null) {
                md5Digest.reset();
                md5Digest.update(buffer);
                byte[] localMd5 = md5Digest.digest();
                if (HashWriter.compareHash(md5, localMd5)) {
                    int offset = mapBlock.get(r32) * HashWriter.blockSize;
                    rNewFile.seek(offset);
                    rNewFile.write(buffer);
                    rangesOk.add(new Range(offset, offset + HashWriter.blockSize));
                }
            }
            v = fb.read();
            start++;
            System.arraycopy(buffer, 1, buffer, 0, buffer.length - 1);
            buffer[buffer.length - 1] = (byte) v;
            checksum.roll((byte) v);
            end++;
        } while (v >= 0);
        fb.close();
        rangesOk.dump();
        final List<Range> unusedRanges = rangesOk.getUnusedRanges();
        DataInputStream zIn = getContent(remotePath, remoteName, unusedRanges, token);
        BufferedOutputStream fOut = new BufferedOutputStream(new FileOutputStream(localFile));
        final int size = unusedRanges.size();
        for (int i = 0; i < size; i++) {
            Range range = unusedRanges.get(i);
            rNewFile.seek(range.getStart());
            byte[] b = new byte[(int) range.size()];
            zIn.readFully(b);
            rNewFile.write(b);
        }
        fOut.close();
        zIn.close();
        rNewFile.close();
        byte[] fileLocalHash = HashWriter.getHash(newFile);
        if (!HashWriter.compareHash(fileHash, fileLocalHash)) {
            throw new IllegalStateException("Partial download failed. Hash error");
        }
        FileUtils.rm(localFile);
        FileUtils.mv(newFile, localFile);
    }
}
