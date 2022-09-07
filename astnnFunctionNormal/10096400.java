class BackupThread extends Thread {
    private void enque(final SocketReadData packet) {
        m_reading.remove(packet.getChannel());
        m_outputQueue.offer(packet);
    }
}
