class BackupThread extends Thread {
            public void run() {
                if (c.getChannelName().equals("SuperPeer")) bucle1(); else if (c.getChannelName().equals("MANET")) bucle();
            }
}
