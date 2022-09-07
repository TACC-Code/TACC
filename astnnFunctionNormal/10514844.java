class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String msg = (String) e.getMessage();
        if (msg.startsWith("QOTM: ")) {
            System.out.println("Quote of the Moment: " + msg.substring(6));
            e.getChannel().close();
        }
    }
}
