class BackupThread extends Thread {
    public static int store(Context context, InputStream is) throws SQLException, IOException {
        String id = Utils.generateKey();
        TableRow bitstream;
        Context tempContext = null;
        try {
            tempContext = new Context();
            bitstream = DatabaseManager.create(tempContext, "Bitstream");
            bitstream.setColumn("deleted", true);
            bitstream.setColumn("internal_id", id);
            bitstream.setColumn("store_number", incoming);
            DatabaseManager.update(tempContext, bitstream);
            tempContext.complete();
        } catch (SQLException sqle) {
            if (tempContext != null) tempContext.abort();
            throw sqle;
        }
        File file = getFile(bitstream);
        File parent = file.getParentFile();
        if (!parent.exists()) parent.mkdirs();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        DigestInputStream dis = null;
        try {
            dis = new DigestInputStream(is, MessageDigest.getInstance("SHA-1"));
        } catch (NoSuchAlgorithmException nsae) {
            log.warn("Caught NoSuchAlgorithmException", nsae);
        }
        Utils.bufferedCopy(dis, fos);
        fos.close();
        is.close();
        bitstream.setColumn("size", (int) file.length());
        bitstream.setColumn("checksum", Utils.toHex(dis.getMessageDigest().digest()));
        bitstream.setColumn("checksum_algorithm", "SHA-1");
        bitstream.setColumn("deleted", false);
        DatabaseManager.update(context, bitstream);
        int bitstream_id = bitstream.getIntColumn("bitstream_id");
        if (log.isDebugEnabled()) log.debug("Stored bitstream " + bitstream_id + " in file " + file.getAbsolutePath());
        return bitstream_id;
    }
}
