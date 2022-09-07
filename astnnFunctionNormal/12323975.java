class BackupThread extends Thread {
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
}
