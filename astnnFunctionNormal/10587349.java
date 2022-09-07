class BackupThread extends Thread {
    private static InputStream getInputStream(InputStream in) throws Throwable {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        while ((read = in.read()) != -1) {
            out.write(read);
        }
        byte[] bytes = out.toByteArray();
        in.close();
        out.close();
        out.flush();
        return new ByteArrayInputStream(bytes);
    }
}
