class BackupThread extends Thread {
    public static void appendText(String content, String path, boolean append) throws IOException {
        FileOutputStream fos;
        FileUtility fu = new FileUtility(path);
        byte[] buffer;
        int bytes_read;
        if (fu.exists() && fu.isDirectory()) {
            abort("directory exists");
        }
        if (!fu.exists() || !fu.isFile()) {
            fos = new FileOutputStream(path);
            buffer = content.getBytes();
            bytes_read = buffer.length;
            fos.write(buffer, 0, bytes_read);
            return;
        }
        if (fu.exists() && fu.isFile()) {
            String originalContent = fu.getContent();
            StringBuilder contentBuf = new StringBuilder();
            if (append) {
                contentBuf.append(originalContent).append("\n").append(content);
            } else {
                contentBuf.append(content).append("\n").append(originalContent);
            }
            if (fu.remove()) {
                fos = new FileOutputStream(fu);
                buffer = contentBuf.toString().getBytes();
                bytes_read = buffer.length;
                fos.write(buffer, 0, bytes_read);
            }
        }
    }
}
