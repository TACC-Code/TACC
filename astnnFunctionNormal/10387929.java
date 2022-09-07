class BackupThread extends Thread {
    public void run() {
        try {
            udpChannel = DatagramChannel.open();
            udpChannel.socket().bind(inetSocketAddress);
            System.out.println("udpChannel.isBlocking() : " + udpChannel.isBlocking());
            ArrayList arIP = new ArrayList();
            List arIPTmp;
            String oldIP = "";
            while (isOnThread && !thread.isInterrupted()) {
                final ByteBuffer[] buffers = bufferPool.getBuffer();
                for (int i = 0, size = buffers.length; i < size; i++) {
                    SocketAddress sa = udpChannel.receive(buffers[i]);
                    String ip = sa.toString();
                    buffers[i].put((byte) '\n');
                    arIP.add(ip);
                    if (!oldIP.equals(ip)) getChannel(ip);
                }
                arIPTmp = (List) arIP.clone();
                arIP.clear();
                threadPoolExecutor.execute(new UnitTask(bufferPool, buffers, Receiver.this, arIPTmp));
            }
        } catch (Exception e) {
            e.printStackTrace();
            _LOG("[Receiver][run] udpChannel Close.");
        }
    }
}
