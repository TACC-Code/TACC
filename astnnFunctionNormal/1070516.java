class BackupThread extends Thread {
    private boolean find(AKSpeak speak, Collection<AKSpeak> possible) {
        for (Iterator<AKSpeak> it = possible.iterator(); it.hasNext(); ) {
            AKSpeak next = it.next();
            if (next.getAgentID().equals(speak.getAgentID()) && next.getChannel() == speak.getChannel() && Arrays.equals(next.getContent(), speak.getContent()) && next.getTime() == speak.getTime()) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
