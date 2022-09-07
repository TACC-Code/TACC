class BackupThread extends Thread {
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("Handshake: " + future.isSuccess(), future.getCause());
                if (future.isSuccess()) {
                    factory.addChannel(future.getChannel());
                } else {
                    future.getChannel().close();
                }
            }
}
