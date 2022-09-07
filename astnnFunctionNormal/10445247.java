class BackupThread extends Thread {
    private void requestRemote(byte[] data, String destHost, int destPort, DataOutputStream outToBrowser) throws IOException {
        String remotePhp = Config.getIns().getRemotePhp();
        log.info("remotePhp: " + remotePhp);
        String requestBase64String = new String(Base64Coder.encode(data, 0, data.length));
        byte[] postData = ("request_data=" + requestBase64String + "&dest_host=" + destHost + "&dest_port=" + destPort).getBytes();
        log.debug("request: " + ByteArrayUtil.toString(postData, 0, postData.length));
        URL remotePhpUrl = new URL(remotePhp);
        HttpURLConnection remotePhpConn = (HttpURLConnection) remotePhpUrl.openConnection();
        remotePhpConn.setRequestMethod("POST");
        remotePhpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        remotePhpConn.setRequestProperty("Connection", "close");
        remotePhpConn.setUseCaches(false);
        remotePhpConn.setDoOutput(true);
        DataOutputStream outToPhp = new DataOutputStream(remotePhpConn.getOutputStream());
        outToPhp.write(postData);
        outToPhp.flush();
        outToPhp.close();
        DataInputStream inFromPhp = new DataInputStream(remotePhpConn.getInputStream());
        int readCount = -1;
        int phpByteSize = Integer.parseInt(Config.getIns().getValue("ptp.buff.size", "1024"));
        byte[] phpByte = new byte[phpByteSize];
        while ((readCount = inFromPhp.read(phpByte, 0, phpByteSize)) != -1) {
            outToBrowser.write(phpByte, 0, readCount);
            outToBrowser.flush();
            log.debug(ByteArrayUtil.toString(phpByte, 0, readCount));
        }
        inFromPhp.close();
    }
}
