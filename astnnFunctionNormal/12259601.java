class BackupThread extends Thread {
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        tmp.add(f.getChannel());
                        Buffer content = new Buffer(16);
                        ev.encode(content);
                        ChannelBuffer msg = ChannelBuffers.wrappedBuffer(content.getRawBuffer(), content.getReadIndex(), content.readableBytes());
                        f.getChannel().write(msg);
                    } else {
                        closeRsocketChannel(token, f.getChannel());
                    }
                }
}
