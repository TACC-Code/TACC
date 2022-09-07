class BackupThread extends Thread {
    public void run() {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            InputStream istream = connection.getInputStream();
            new File(filename).getParentFile().mkdirs();
            FileOutputStream ostream = new FileOutputStream(filename);
            byte[] buffer = new byte[8192];
            int size;
            while ((size = istream.read(buffer)) > 0) ostream.write(buffer, 0, size);
            ostream.close();
            istream.close();
            queue.enqueue(event);
        } catch (Exception exception) {
            event.exception = exception;
            queue.enqueue(event);
        }
    }
}
