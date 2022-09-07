class BackupThread extends Thread {
    byte[] digestInterleaved(byte[] ba) {
        int limit = ba.length;
        int i, offset;
        for (offset = 0; offset < limit && ba[offset] == 0x00; offset++) ;
        int klen = (limit - offset) / 2;
        byte[] result = new byte[2 * digestLength];
        MessageDigest mdEven = newDigest();
        MessageDigest mdOdd = newDigest();
        int j = limit - 1;
        for (i = 0; i < klen; i++) {
            mdEven.update(ba[j--]);
            mdOdd.update(ba[j--]);
        }
        byte[] outEven = mdEven.digest();
        byte[] outOdd = mdOdd.digest();
        for (i = 0, j = 0; i < digestLength; i++) {
            result[j++] = outEven[i];
            result[j++] = outOdd[i];
        }
        return result;
    }
}
