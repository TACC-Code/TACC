class BackupThread extends Thread {
    public static Future<Void> drain(final InputStream source, final OutputStream target) {
        return EXEC_SVC.submit(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                try {
                    try {
                        byte[] buf = new byte[4096];
                        int read;
                        while ((read = source.read(buf)) != -1) {
                            target.write(buf, 0, read);
                        }
                    } finally {
                        target.flush();
                        target.close();
                    }
                } finally {
                    source.close();
                }
                return null;
            }
        });
    }
}
