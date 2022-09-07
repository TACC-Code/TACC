class BackupThread extends Thread {
    public void broadcastToLearners(Proposal p, MessageEvent e) {
        for (PaxosProcess l : pCtx.getLearners().values()) {
            logger.info("Sending ballot to learner: " + l.getIpAddr() + ":" + l.getPort());
            e.getChannel().write(PaxosUtils.createStringFromProposal(p), l.getInetSocketAddress());
        }
    }
}
