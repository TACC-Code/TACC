class BackupThread extends Thread {
    public ClientInfo(byte[] client_pkey_value, int num_extents, int max_num_extents, long min_expire_time, long max_expire_time, MessageDigest md) {
        _client_pkey_value = client_pkey_value;
        md.update(client_pkey_value);
        _client_id = byteArrayToBigInteger(md.digest());
        _num_extents = num_extents;
        _max_num_extents = _max_num_extents;
        _min_expire_time = min_expire_time;
        _max_expire_time = max_expire_time;
    }
}
