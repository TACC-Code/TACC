class BackupThread extends Thread {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        if (f.isSuccess()) {
                            f.getChannel().write(request);
                            if (logger.isDebugEnabled()) {
                                logger.debug("Write pull request:" + request);
                            }
                        }
                    }
}
