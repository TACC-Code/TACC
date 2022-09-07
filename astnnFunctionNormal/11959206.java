class BackupThread extends Thread {
    private void push(byte[] data) {
        if (!nodes.isEmpty()) {
            for (int i = 0; i < nodes.size(); i++) {
                byte[] node = nodes.get(i);
                if (node == MARKER) {
                    nodes.set(i, data);
                    return;
                }
                internalDigest.reset();
                internalDigest.update((byte) 1);
                internalDigest.update(node);
                internalDigest.update(data);
                data = internalDigest.digest();
                nodes.set(i, MARKER);
            }
        }
        nodes.add(data);
    }
}
