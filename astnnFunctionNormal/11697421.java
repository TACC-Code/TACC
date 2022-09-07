class BackupThread extends Thread {
    private void downloadFile() throws IOException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                stats.print();
            }
        }, 1000, 1000);
        try {
            System.out.println(new Date() + " Opening Streams");
            InputStream in = url.openStream();
            OutputStream out = new FileOutputStream(outputFile);
            System.out.println(new Date() + " Streams opened");
            byte[] buf = new byte[1024 * 1024];
            int length;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
                stats.bytes(length);
            }
            in.close();
            out.close();
        } finally {
            timer.cancel();
            stats.print();
        }
    }
}
