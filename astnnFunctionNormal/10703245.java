class BackupThread extends Thread {
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String incoming = (String) e.getMessage();
        Proposal propIn = PaxosUtils.createProposalFromString(incoming);
        Proposal propResp = new Proposal();
        propResp.setSrcId(pCtx.getSrcId());
        propResp.setBallot(propIn.getBallot());
        if (propIn.getBallot() > pCtx.getBallot()) {
            logger.info("Got ballot " + propIn.getBallot());
            pCtx.setBallot(propIn.getBallot());
            e.getChannel().write(PaxosUtils.createStringFromProposal(propResp), e.getRemoteAddress());
            pCtx.setValue(null);
        } else if (propIn.getBallot() == pCtx.getBallot() && pCtx.getValue() == null) {
            logger.info("Learned value : " + propIn.getValue());
            pCtx.setValue(propIn.getValue());
            propResp.setValue(propIn.getValue());
            pCtx.getProposalHistory().put(propIn.getBallot(), propIn);
            propIn.setSrcId(pCtx.getSrcId());
            broadcastToLearners(propIn, e);
            e.getChannel().write(PaxosUtils.createStringFromProposal(propResp), e.getRemoteAddress());
        } else {
            logger.info("Ignoring received ballotId " + propIn.getBallot());
        }
    }
}
