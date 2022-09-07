class BackupThread extends Thread {
    private boolean receiveData(File f) throws IOException {
        int count = in.readInt();
        int n = in.readInt();
        int remainder = in.readInt();
        long offset = 0;
        byte[] file_sum1, file_sum2;
        byte[] data = new byte[CHUNK_SIZE];
        logger.debug("receiveData count=" + count + " n=" + n + " remainder=" + remainder);
        recvConfig.blockLength = n;
        DeltaDecoder deltasIn = new PlainDeltaDecoder(recvConfig, in);
        RebuilderStream rebuilder = new RebuilderStream();
        rebuilder.addListener(this);
        try {
            rebuilder.setBasisFile(f);
        } catch (FileNotFoundException fnfe) {
        }
        Delta delta = null;
        File newf = null;
        if (f.getParentFile() != null) newf = File.createTempFile(".jarsync", ".tmp", f.getParentFile()); else newf = File.createTempFile(".jarsync", ".tmp", new File(System.getProperty("user.dir")));
        rebuildFile = new RandomAccessFile(newf, "rw");
        try {
            int i = 0;
            while ((delta = deltasIn.read()) != null) {
                i++;
                if (i == count && (delta instanceof Offsets) && remainder > 0) {
                    delta = new Offsets(((Offsets) delta).getOldOffset(), ((Offsets) delta).getNewOffset(), remainder);
                }
                if (delta instanceof Offsets) stats.matched_data += delta.getBlockLength(); else stats.literal_data += delta.getBlockLength();
                rebuilder.update(delta);
            }
        } catch (ListenerException le) {
            throw (IOException) le.getCause();
        }
        rebuilder.doFinal();
        rebuildFile.close();
        if (!newf.renameTo(f)) {
            throw new IOException("cannot rename " + newf + " to " + f);
        }
        if (remoteVersion >= 14) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("BrokenMD4");
            } catch (NoSuchAlgorithmException nsae) {
                logger.fatal("could not create message digest");
                throw new Error(nsae);
            }
            FileInputStream fin = new FileInputStream(f);
            int i = 0;
            md.update(recvConfig.checksumSeed);
            while ((i = fin.read(data)) != -1) {
                md.update(data, 0, i);
            }
            file_sum1 = md.digest();
            file_sum2 = new byte[file_sum1.length];
            in.read(file_sum2);
            logger.debug("file_sum1=" + Util.toHexString(file_sum1));
            logger.debug("file_sum2=" + Util.toHexString(file_sum2));
            return Arrays.equals(file_sum1, file_sum2);
        }
        return true;
    }
}
