class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        final Object o = e.getMessage();
        final Channel channel = e.getChannel();
        final RtmpMessage message = (RtmpMessage) o;
        switch(message.getHeader().getMessageType()) {
            case CONTROL:
                Control control = (Control) message;
                logger.debug("server control: {}", control);
                switch(control.getType()) {
                    case PING_REQUEST:
                        final int time = control.getTime();
                        logger.debug("server ping: {}", time);
                        Control pong = Control.pingResponse(time);
                        logger.debug("sending ping response: {}", pong);
                        channel.write(pong);
                        break;
                    case SWFV_REQUEST:
                        if (swfvBytes == null) {
                            logger.warn("swf verification not initialized!" + " not sending response, server likely to stop responding / disconnect");
                        } else {
                            Control swfv = Control.swfvResponse(swfvBytes);
                            logger.info("sending swf verification response: {}", swfv);
                            channel.write(swfv);
                        }
                        break;
                    default:
                        logger.debug("ignoring control message: {}", control);
                }
                break;
            case METADATA_AMF0:
            case METADATA_AMF3:
                Metadata metadata = (Metadata) message;
                if (metadata.getName().equals("onMetaData")) {
                    logger.info("writing server 'onMetaData': {}", metadata);
                    writer.write(message);
                } else {
                    logger.info("ignoring server metadata: {}", metadata);
                }
                break;
            case AUDIO:
            case VIDEO:
            case AGGREGATE:
                writer.write(message);
                bytesRead += message.getHeader().getSize();
                if ((bytesRead - bytesReadLastSent) > bytesReadWindow) {
                    logger.info("sending bytes read ack {}", bytesRead);
                    bytesReadLastSent = bytesRead;
                    channel.write(new BytesRead(bytesRead));
                }
                break;
            case COMMAND_AMF0:
            case COMMAND_AMF3:
                Command command = (Command) message;
                String name = command.getName();
                logger.info("server command: {}", name);
                if (name.equals("_result")) {
                    String resultFor = transactionToCommandMap.get(command.getTransactionId());
                    logger.info("result for method call: {}", resultFor);
                    if (resultFor.equals("connect")) {
                        writeCommandExpectingResult(channel, Command.createStream());
                    } else if (resultFor.equals("createStream")) {
                        final int streamId = ((Double) command.getArg(0)).intValue();
                        logger.info("streamId to play: {}", streamId);
                        channel.write(Command.play(streamId, session));
                    } else {
                        logger.warn("un-handled server result for: {}", resultFor);
                    }
                } else if (name.equals("onStatus")) {
                    Map<String, Object> temp = (Map) command.getArg(0);
                    String code = (String) temp.get("code");
                    logger.info("onStatus code: {}", code);
                    if (code.equals("NetStream.Failed") || code.equals("NetStream.Play.Failed") || code.equals("NetStream.Play.Stop")) {
                        logger.info("disconnecting, bytes read: {}", bytesRead);
                        writer.close();
                        channel.close();
                    }
                } else {
                    logger.warn("ignoring server command: {}", command);
                }
                break;
            case BYTES_READ:
                logger.info("server bytes read: {}", message);
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
                logger.info("ignoring rtmp message: {}", message);
        }
    }
}
