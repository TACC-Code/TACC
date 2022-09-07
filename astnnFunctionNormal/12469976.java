class BackupThread extends Thread {
    protected void doRealize() throws MediaException {
        qsmc.open();
        stopped = true;
        if (source != null) {
            int count;
            byte[] b = new byte[bufferSize];
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
            try {
                while ((count = source.read(b, 0, bufferSize)) > 0) baos.write(b, 0, count);
                boolean r = qsmc.fillBuffer(baos.toByteArray());
                baos.close();
                if (!r) throw new MediaException("Bad Tone Format");
            } catch (IOException ioe) {
                throw new MediaException("Failure occured with read stream");
            }
            baos = null;
        }
    }
}
