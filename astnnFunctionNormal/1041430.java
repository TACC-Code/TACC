class BackupThread extends Thread {
    @Override
    public void sessionOpened(IoSession session) {
        session.setAttachment(server.getChannelName() + "-" + numClient);
        numClient++;
        logger.info(session.getAttachment() + "| Cliente conectado: " + session.getRemoteAddress() + "|");
    }
}
