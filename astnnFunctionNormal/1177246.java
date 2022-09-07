class BackupThread extends Thread {
    @Override
    public byte[] read(DataInput dataInput, byte[] object) throws IOException {
        ZipInputStream zis = new ZipInputStream(new DataInputInputStream(dataInput));
        zis.getNextEntry();
        if (object != null && zis.read(object) < object.length) return object;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (object != null) baos.write(object);
        if (object == null || object.length < 512) object = new byte[512];
        int read;
        do {
            read = zis.read(object);
            baos.write(object, 0, Math.max(0, read));
        } while (read == object.length);
        return baos.toByteArray();
    }
}
