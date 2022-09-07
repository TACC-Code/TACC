class BackupThread extends Thread {
    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent me) {
        if (publisher != null && publisher.handle(me)) {
            return;
        }
        final Channel channel = me.getChannel();
        final RtmpMessage message = (RtmpMessage) me.getMessage();
        bytesRead += message.getHeader().getSize();
        if ((bytesRead - bytesReadLastSent) > bytesReadWindow) {
            logger.info("sending bytes read ack after: {}", bytesRead);
            BytesRead ack = new BytesRead(bytesRead);
            channel.write(ack);
            bytesReadLastSent = bytesRead;
        }
        switch(message.getHeader().getMessageType()) {
            case CHUNK_SIZE:
                break;
            case CONTROL:
                final Control control = (Control) message;
                switch(control.getType()) {
                    case SET_BUFFER:
                        logger.debug("received set buffer: {}", control);
                        bufferDuration = control.getBufferLength();
                        if (publisher != null) {
                            publisher.setBufferDuration(bufferDuration);
                        }
                        break;
                    default:
                        logger.info("ignored control: {}", control);
                }
                break;
            case COMMAND_AMF0:
            case COMMAND_AMF3:
                final Command command = (Command) message;
                final String name = command.getName();
                if (name.equals("connect")) {
                    connectResponse(channel, command);
                } else if (name.equals("createStream")) {
                    streamId = 1;
                    channel.write(Command.createStreamSuccess(command.getTransactionId(), streamId));
                } else if (name.equals("play")) {
                    playResponse(channel, command);
                } else if (name.equals("deleteStream")) {
                    int deleteStreamId = ((Double) command.getArg(0)).intValue();
                    logger.info("deleting stream id: {}", deleteStreamId);
                } else if (name.equals("closeStream")) {
                    final int clientStreamId = command.getHeader().getStreamId();
                    logger.info("closing stream id: {}", clientStreamId);
                    unpublishIfLive();
                } else if (name.equals("pause")) {
                    pauseResponse(channel, command);
                } else if (name.equals("seek")) {
                    seekResponse(channel, command);
                } else if (name.equals("publish")) {
                    publishResponse(channel, command);
                } else {
                    logger.warn("ignoring command: {}", command);
                    fireNext(channel);
                }
                return;
            case METADATA_AMF0:
            case METADATA_AMF3:
                final Metadata meta = (Metadata) message;
                if (meta.getName().equals("onMetaData")) {
                    logger.info("adding onMetaData message: {}", meta);
                    meta.setDuration(-1);
                    subscriberStream.addConfigMessage(meta);
                }
                broadcast(message);
                break;
            case AUDIO:
            case VIDEO:
                if (((DataMessage) message).isConfig()) {
                    logger.info("adding config message: {}", message);
                    subscriberStream.addConfigMessage(message);
                }
            case AGGREGATE:
                broadcast(message);
                break;
            case BYTES_READ:
                final BytesRead bytesReadByClient = (BytesRead) message;
                bytesWrittenLastReceived = bytesReadByClient.getValue();
                logger.debug("bytes read ack from client: {}, actual: {}", bytesReadByClient, bytesWritten);
                break;
            case WINDOW_ACK_SIZE:
                WindowAckSize was = (WindowAckSize) message;
                if (was.getValue() != bytesReadWindow) {
                    channel.write(SetPeerBw.dynamic(bytesReadWindow));
                }
                break;
            case SET_PEER_BW:
                SetPeerBw spb = (SetPeerBw) message;
                if (spb.getValue() != bytesWrittenWindow) {
                    channel.write(new WindowAckSize(bytesWrittenWindow));
                }
                break;
            default:
                logger.warn("ignoring message: {}", message);
        }
        fireNext(channel);
    }
}
