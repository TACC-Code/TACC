class BackupThread extends Thread {
    public static boolean login(SocketAddress tcp, SocketAddress udp, String mailAddress, String password) {
        if (!TCPController.connect(tcp)) return false;
        if (!UDPController.connect(udp)) return false;
        return TCPController.login(mailAddress, password);
    }
}
