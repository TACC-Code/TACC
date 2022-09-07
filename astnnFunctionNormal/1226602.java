class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        BigInteger number;
        if (e.getMessage() instanceof BigInteger) {
            number = (BigInteger) e.getMessage();
        } else {
            number = new BigInteger(e.getMessage().toString());
        }
        lastMultiplier = number.intValue();
        factorial = factorial.multiply(number);
        e.getChannel().write(factorial);
    }
}
