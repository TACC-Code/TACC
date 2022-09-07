class BackupThread extends Thread {
    public void run() {
        MetroClient cl = new MetroClient();
        cl.setMsgCallref(this);
        cl.setSysCallref(this);
        cl.connect("localhost", 4242);
        Console con = System.console();
        String cmd = "";
        System.out.print("Name :");
        cmd = con.readLine();
        cl.login(cmd);
        while (keepalive) {
            System.out.print("Nachricht :");
            cmd = con.readLine();
            if (cmd != "exit") cl.send(Encoder.encodeBroadcast(cmd.getBytes())); else keepalive = false;
        }
        cl.disconnect();
    }
}
