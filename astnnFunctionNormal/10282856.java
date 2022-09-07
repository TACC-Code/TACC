class BackupThread extends Thread {
    public void setConnection(Socket socket) {
        this.socket = socket;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "reader and writer error", "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        jButton2.setText("Disconnect");
        jTextField2.setEditable(false);
        jTextField1.setEditable(false);
        jButton1.setEnabled(true);
        setTitle("P2PChat: CONNECTED to " + socket.getInetAddress().toString().split("/")[1]);
        jTextField2.setText(socket.getInetAddress().toString().split("/")[1]);
    }
}
