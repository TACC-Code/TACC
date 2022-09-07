class BackupThread extends Thread {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("usage: jist mem_events <events>");
            return;
        }
        int num = Integer.parseInt(args[0]);
        mem_events e = new mem_events();
        for (int i = 0; i < num; i++) {
            e.process();
        }
        System.gc();
        System.out.println("freemem:  " + Runtime.getRuntime().freeMemory());
        System.out.println("maxmem:   " + Runtime.getRuntime().maxMemory());
        System.out.println("totalmem: " + Runtime.getRuntime().totalMemory());
        System.out.println("used:     " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        try {
            byte[] b = new byte[5000];
            FileInputStream fin = new FileInputStream("/proc/self/status");
            int readbytes = fin.read(b);
            System.out.write(b, 0, readbytes);
        } catch (IOException ex) {
            JistAPI.end();
        }
    }
}
