class BackupThread extends Thread {
    private byte[] transformClass(String className, TransformationResult result) {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/" + className.replace('.', '/') + ".class");
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] byteArray = new byte[1024];
            int readBytes = -1;
            while ((readBytes = resourceAsStream.read(byteArray, 0, byteArray.length)) != -1) out.write(byteArray, 0, readBytes);
            resourceAsStream.close();
            return this.transformer.transformClass(out.toByteArray(), className, this.cl, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
