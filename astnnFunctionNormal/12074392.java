class BackupThread extends Thread {
    public boolean putProperties(String key, Properties props) throws Exception {
        InetAddress gateway = InetAddress.getByName(DHT_GW_ADDR);
        int port = Integer.parseInt(DHT_GW_PORT);
        bamboo_put_arguments putArgs = new bamboo_put_arguments();
        putArgs.application = Put.class.getName();
        putArgs.client_library = "Remote Tea ONC/RPC";
        MessageDigest md = MessageDigest.getInstance("SHA");
        putArgs.key = new bamboo_key();
        putArgs.key.value = md.digest(key.getBytes());
        putArgs.value = new bamboo_value();
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        props.store(array, key);
        putArgs.value.value = array.toByteArray();
        putArgs.ttl_sec = Integer.parseInt(DHT_TTL);
        putArgs.secret_hash = new bamboo_hash();
        putArgs.secret_hash.algorithm = "SHA";
        putArgs.secret_hash.hash = md.digest(DHT_SECRET.getBytes());
        gateway_protClient client = new gateway_protClient(gateway, port, ONCRPC_TCP);
        int result = client.BAMBOO_DHT_PROC_PUT_3(putArgs);
        if (result == 0) {
            System.out.println("BAMBOO_OK");
            return true;
        } else {
            return true;
        }
    }
}
