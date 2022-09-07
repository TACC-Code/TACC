class BackupThread extends Thread {
    private static byte[] _part4Hash(String target, byte[] magicValue, boolean hackSha1) {
        byte[] xor1 = _part4Xor(target, 0x36);
        byte[] xor2 = _part4Xor(target, 0x5c);
        SHA1 sha1 = new SHA1();
        sha1.update(xor1);
        if (hackSha1) sha1.setBitCount(0x1ff);
        sha1.update(magicValue);
        byte[] digest1 = sha1.digest();
        sha1.reset();
        sha1.update(xor2);
        sha1.update(digest1);
        byte[] digest2 = sha1.digest();
        return digest2;
    }
}
