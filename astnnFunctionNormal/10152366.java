class BackupThread extends Thread {
    public void send(Packet packet) throws IOException {
        if (packet instanceof SSHPacket) {
            System.out.println("Sending SSH packet...");
            String macKey = "";
            HMACSHA1 mac = new HMACSHA1();
            byte[] code = mac.compute(macKey, this.getSequenceNumber(), (SSHPacket) packet);
            ((SSHPacket) packet).mac = code;
            if (this.getCompressor() != null) {
                packet = (SSHPacket) this.getCompressor().compress(packet);
            }
            ByteBuffer data = packet.getData();
            System.out.println(packet);
            this.getChannel().write(data);
            this.sequenceNumber++;
        } else {
            System.out.println("Sending plain packet...");
            ByteBuffer data = packet.getData();
            System.out.println(packet);
            this.getChannel().write(data);
        }
    }
}
