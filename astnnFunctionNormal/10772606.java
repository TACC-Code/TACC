class BackupThread extends Thread {
    public boolean checkPiece(int piece, byte[] bs, int off, int length) {
        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("No SHA digest available: " + nsae);
        }
        sha1.update(bs, off, length);
        byte[] hash = sha1.digest();
        for (int i = 0; i < 20; i++) {
            if (hash[i] != piece_hashes[20 * piece + i]) {
                return false;
            }
        }
        return true;
    }
}
