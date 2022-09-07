class BackupThread extends Thread {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    try {
                        ChannelFutureListener.CLOSE.operationComplete(f);
                    } catch (Throwable t) {
                        LOG.error("ERROR While closing channel :" + f.getChannel() + " " + f.getCause());
                    }
                }
}
