class BackupThread extends Thread {
        protected byte[] loadTileInBuffer(URLConnection urlConn) throws IOException {
            input = urlConn.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream(input.available());
            byte[] buffer = new byte[2048];
            boolean finished = false;
            do {
                int read = input.read(buffer);
                if (read >= 0) bout.write(buffer, 0, read); else finished = true;
            } while (!finished);
            if (bout.size() == 0) return null;
            return bout.toByteArray();
        }
}
