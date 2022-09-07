class BackupThread extends Thread {
    protected final void runFcgi(Map env, String php, boolean includeJava) {
        int c;
        byte buf[] = new byte[Util.BUF_SIZE];
        try {
            Process proc = doBind(env, php, includeJava);
            if (proc == null || proc.getInputStream() == null) return;
            proc.getInputStream().close();
            InputStream in = proc.getErrorStream();
            while ((c = in.read(buf)) != -1) System.err.write(buf, 0, c);
            try {
                in.close();
            } catch (IOException e) {
            }
        } catch (Exception e) {
            lastException = e;
            System.err.println("Could not start FCGI server: " + e);
        }
        ;
    }
}
