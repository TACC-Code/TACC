class BackupThread extends Thread {
    public static ByteBuffer fileContent(String path) {
        File file = new File(WEB_ROOT + path);
        if (!file.exists()) throw RoughHttpError.HTTP_404;
        if (file.isDirectory()) {
            if (path.endsWith(PATH_SEPERATOR)) {
                file = new File(WEB_ROOT + path + DIRECTORY_INDEX);
                if (!file.exists()) throw RoughHttpError.HTTP_403;
            } else {
                RoughHttpError e = new RoughHttpError(302);
                e.extras().put("Location", path + PATH_SEPERATOR);
                throw e;
            }
        }
        if (!file.canRead()) throw RoughHttpError.HTTP_403;
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw RoughHttpError.HTTP_404;
        }
        FileChannel channel = stream.getChannel();
        MappedByteBuffer buffer;
        try {
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        } catch (IOException e) {
            throw RoughHttpError.HTTP_500;
        }
        return buffer.load();
    }
}
