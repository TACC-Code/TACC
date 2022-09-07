class BackupThread extends Thread {
    public void fillAttributes(Set<MDAttribute> readSet, Set<MDAttribute> writeSet, MDObject obj) throws Exception {
        for (MDActor actor : ownActors) {
            actor.fillAttributes(readSet, writeSet, obj);
        }
        if (getParent() != null) getParent().fillAttributes(readSet, writeSet, obj);
    }
}
