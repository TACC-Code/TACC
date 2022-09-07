class BackupThread extends Thread {
    public void append(final StringBuilder b) {
        for (int i = 0; i < size(); i++) {
            ChannelChange change = get(i);
            if ((i % 10) == 0) {
                b.append("\n     ");
            }
            b.append(" ");
            b.append(change.getChannelId());
            b.append("[");
            b.append(change.getDmxValue());
            b.append("]");
        }
        b.append('\n');
    }
}
