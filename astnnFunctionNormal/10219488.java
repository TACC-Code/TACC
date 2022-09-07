class BackupThread extends Thread {
    public void close() throws IOException {
        if (closed) return;
        out.close();
        closed = true;
        dvals = new Stack();
        while (!digests.empty()) {
            byte[] dval = ctx.digest();
            ctx = (Digest) digests.pop();
            ctx.update(dval);
            dvals.push(dval);
        }
        initialDigest = ctx.digest();
        int parts = (int) ((b.size() - 1) / partSize);
        long lastPart = b.size() - parts * partSize;
        totalLength = parts * (partSize + 21) + lastPart + 1;
    }
}
