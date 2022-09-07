class BackupThread extends Thread {
    @Overrides
    public void toOutputStream(int fromIndex, int toIndex, OutputStream dest) throws IOException {
        if ((toIndex - fromIndex > 10) && (dest instanceof FileOutputStream)) {
            toByteChannel(fromIndex, toIndex, ((FileOutputStream) dest).getChannel());
        } else {
            CheckArg.range(size(), fromIndex, toIndex);
            int remaining = toIndex - fromIndex;
            if (remaining <= 16) {
                while (fromIndex < toIndex) dest.write(get(fromIndex++));
            } else {
                int bufLen = Math.min(1024, remaining);
                byte[] buf = new byte[bufLen];
                while (fromIndex < toIndex) {
                    int step = Math.min(bufLen, remaining);
                    toArray(fromIndex, fromIndex + step, buf, 0);
                    dest.write(buf);
                    fromIndex += step;
                }
            }
        }
    }
}
