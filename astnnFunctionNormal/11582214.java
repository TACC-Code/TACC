class BackupThread extends Thread {
                public void operationComplete(ChannelFuture arg0) throws Exception {
                    channel = arg0.getChannel();
                    sendOptions();
                }
}
