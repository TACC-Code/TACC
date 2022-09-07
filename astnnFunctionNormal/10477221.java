class BackupThread extends Thread {
    public static Object deepclone(final Serializable o) throws IOException, ClassNotFoundException {
        final PipedOutputStream pipeout = new PipedOutputStream();
        PipedInputStream pipein = new PipedInputStream(pipeout);
        Thread writer = new Thread() {

            public void run() {
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(pipeout);
                    out.writeObject(o);
                } catch (IOException e) {
                } finally {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        };
        writer.start();
        ObjectInputStream in = new ObjectInputStream(pipein);
        return in.readObject();
    }
}
