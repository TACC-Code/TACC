class BackupThread extends Thread {
    public void run() {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
        }
        while (dos != null) {
            while (q.size() > 0) {
                byte[] b = q.poll();
                try {
                    dos.writeShort(Short.MIN_VALUE + b.length);
                    s.getOutputStream().write(b);
                } catch (IOException e) {
                    System.out.println("tcp write thread io exception, cannot write to socket stream");
                    break;
                }
            }
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("tcp write thread interrupted exception caught, breaking loop");
                    break;
                }
            }
        }
        System.out.println("tcp write thread terminated");
    }
}
