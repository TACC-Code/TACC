class BackupThread extends Thread {
    private byte[] collapse() {
        byte[] last = null;
        for (int i = 0; i < nodes.size(); i++) {
            byte[] current = nodes.get(i);
            if (current == MARKER) continue;
            if (last == null) last = current; else {
                internalDigest.reset();
                internalDigest.update((byte) 1);
                internalDigest.update(current);
                internalDigest.update(last);
                last = internalDigest.digest();
            }
            nodes.set(i, MARKER);
        }
        assert (last != null);
        return last;
    }
}
