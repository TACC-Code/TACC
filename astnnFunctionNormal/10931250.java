class BackupThread extends Thread {
    @Primitive
    public static Value unix_connect(final CodeRunner ctxt, final Value socket, final Value addr) throws Fail.Exception, FalseExit {
        final Context context = ctxt.getContext();
        final Channel ch = context.getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "connect", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final Block addrBlock = addr.asBlock();
        try {
            context.enterBlockingSection();
            ch.socketConnect(new InetSocketAddress(InetAddress.getByAddress(addrBlock.get(0).asBlock().getBytes()), addrBlock.get(1).asLong()));
            context.leaveBlockingSection();
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(context);
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            context.leaveBlockingSection();
            Unix.fail(ctxt, "connect", ioe);
        }
        return Value.UNIT;
    }
}
