class BackupThread extends Thread {
    private Map writeFilePart(FilePart part, OutputStream out) throws IOException {
        boolean isAscii = true;
        long size = 0;
        if (part.getContentType().equals("application/x-macbinary")) {
            out = new MacBinaryDecoderOutputStream(out);
        }
        int read;
        byte[] buf = new byte[8 * 1024];
        while ((read = part.getInputStream().read(buf)) != -1) {
            out.write(buf, 0, read);
            if (isAscii) {
                for (int i = 0; i < read; i++) {
                    if (buf[i] >= 127) {
                        isAscii = false;
                        break;
                    }
                }
            }
            size += read;
        }
        Map result = new HashMap();
        result.put("size", size);
        result.put("isAscii", isAscii);
        return result;
    }
}
