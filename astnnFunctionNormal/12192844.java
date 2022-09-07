class BackupThread extends Thread {
    public void doSync(Interest interest, Interest readInterest) throws IOException {
        RepositoryDataListener listener = null;
        synchronized (_currentListeners) {
            if (isDuplicateRequest(interest)) return;
            if (Log.isLoggable(Log.FAC_REPO, Level.FINER)) Log.finer(Log.FAC_REPO, "Repo checked write no content for {0}, starting read", interest.name());
            listener = new RepositoryDataListener(interest, readInterest, this);
            addListener(listener);
            listener.getInterests().add(readInterest, null);
        }
        _handle.expressInterest(readInterest, listener);
    }
}
