class BackupThread extends Thread {
    public void handle(NetworkManager man, DatagramSession session, Packet packet) throws Exception {
        if (getStore().getChannel() == null) return;
        Voice v = new Voice();
        v.deserialize(packet.getBuf(), Protocol.UDP_CURSOR);
        PeerBean bean = getPeers().get(v.id);
        if (bean == null) return;
        Speaker speaker = (Speaker) bean.getData();
        if (speaker == null) return;
        byte[] data = new byte[320];
        System.arraycopy(v.data, 0, data, 0, v.data.length);
        speaker.write(data, 62);
    }
}
