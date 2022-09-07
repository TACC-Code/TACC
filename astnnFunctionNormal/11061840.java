class BackupThread extends Thread {
    @Primitive
    public static Value unix_bind(final CodeRunner ctxt, final Value socket, final Value addr) throws Fail.Exception, FalseExit {
        final Channel ch = ctxt.getContext().getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "bind", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final Block addrBlock = addr.asBlock();
        try {
            ch.socketBind(new InetSocketAddress(InetAddress.getByAddress(addrBlock.get(0).asBlock().getBytes()), addrBlock.get(1).asLong()));
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            Unix.fail(ctxt, "bind", ioe);
        }
        return Value.UNIT;
    }
}
