class BackupThread extends Thread {
    protected int value_hash_code() {
        if (_hashcached) return hashcode;
        _hashcached = true;
        if (_value == null) return hashcode = 0;
        byte[] res = md5.digest(_value);
        return hashcode = res[0] << 24 + res[1] << 16 + res[2] << 8 + res[3];
    }
}
