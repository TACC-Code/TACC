class BackupThread extends Thread {
    public void run() {
        try {
            while (!disconnecting) {
                try {
                    while (q.size() > 0 && !disconnecting) {
                        dataSem.acquire();
                        WriteData wd = q.poll();
                        byte[] buff = wd.getData();
                        m.remove(wd.getObject());
                        dataSem.release();
                        DatagramPacket packet = new DatagramPacket(buff, buff.length, group, ms.getLocalPort());
                        ms.send(packet);
                    }
                    if (!disconnecting) {
                        synchronized (this) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("terminating udp write thread");
    }
}
