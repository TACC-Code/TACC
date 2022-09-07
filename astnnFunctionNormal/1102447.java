class BackupThread extends Thread {
    public String getChannelName() {
        String encodedAddr = "";
        String address = file;
        if (address != null) {
            encodedAddr = "" + address;
            if (encodedAddr.startsWith("/")) encodedAddr = encodedAddr.substring(1);
            encodedAddr = URLEncoder.encode(encodedAddr);
        }
        return ("jk-" + encodedAddr);
    }
}
