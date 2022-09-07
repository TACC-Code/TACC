class BackupThread extends Thread {
    public KarmaListener getListener(Channel c) {
        for (KarmaListener l : listeners) {
            if (l.getChannel().equals(c)) {
                return l;
            }
        }
        return null;
    }
}
