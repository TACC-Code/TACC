class BackupThread extends Thread {
    private void flush(LinkedList<Request> toFlush) throws IOException {
        if (toFlush.size() == 0) {
            return;
        }
        LinkedList<FileOutputStream> streamsToFlushNow;
        synchronized (streamsToFlush) {
            streamsToFlushNow = (LinkedList<FileOutputStream>) streamsToFlush.clone();
        }
        for (FileOutputStream fos : streamsToFlushNow) {
            fos.flush();
            if (forceSync) {
                ((FileChannel) fos.getChannel()).force(false);
            }
        }
        while (streamsToFlushNow.size() > 1) {
            FileOutputStream fos = streamsToFlushNow.removeFirst();
            fos.close();
            synchronized (streamsToFlush) {
                streamsToFlush.remove(fos);
            }
        }
        while (toFlush.size() > 0) {
            Request i = toFlush.remove();
            nextProcessor.processRequest(i);
        }
    }
}
