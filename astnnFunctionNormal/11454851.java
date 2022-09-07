class BackupThread extends Thread {
    public MessageContext createContext(String currentTrigger, MessageEvent event) {
        MessageContextImpl.Builder builder = MessageContextImpl.Builder.newBuilder();
        String target = null;
        String msg = null;
        String operation = null;
        final String rawText = event.getMessage();
        final Channel channel = event.getChannel();
        final String channelName = (channel != null) ? channel.getName() : null;
        if (event.isPrivate()) {
            currentTrigger = "";
        }
        final int triggerLength = currentTrigger.length();
        Log.debug("Trigger Length:" + triggerLength);
        if (triggerLength == JerkBotConstants.DOUBLE_TRIGGER_LEN) {
            Log.debug("Double trigger");
            String messageContents = rawText.substring(triggerLength);
            StringTokenizer tokenizer = new StringTokenizer(messageContents);
            if (tokenizer.hasMoreTokens()) {
                target = tokenizer.nextToken();
                if (!event.isPrivate()) {
                    if (!channel.getNicks().contains(target)) {
                        target = event.getNick();
                    }
                }
                Log.debug("Target:{}", target);
                String subMessage = messageContents.substring(target.length()).trim();
                Log.debug("SubMessage:{}", subMessage);
                StringTokenizer subTokenizer = new StringTokenizer(subMessage);
                Log.debug("SubTokenizerLen:{}", subTokenizer.countTokens());
                if (subTokenizer.countTokens() > 1) {
                    int operationIndex = subMessage.indexOf(" ");
                    operation = subMessage.substring(0, operationIndex);
                    Log.debug("operation:{}", operation);
                    msg = subMessage.substring(operation.length() + 1);
                    Log.debug("msg:{}", msg);
                } else {
                    operation = subMessage;
                }
            }
        } else {
            Log.debug("Single trigger");
            String subMessage = rawText.substring(triggerLength);
            StringTokenizer tokenizer = new StringTokenizer(subMessage);
            if (tokenizer.hasMoreTokens()) {
                operation = tokenizer.nextToken();
                final int operationLength = operation.length();
                if ((operationLength + 1) < subMessage.length()) {
                    msg = subMessage.substring(operationLength + 1);
                }
                target = event.getNick();
                if (msg != null) {
                    msg = msg.trim();
                }
            }
        }
        if (operation != null) {
            operation = operation.toLowerCase();
        }
        MessageContext ctx = builder.channel(channelName).hostname(event.getHostName()).username(event.getUserName()).sender(event.getNick()).message(event.getMessage()).parsedMessage(msg).userTarget(target).commandName(operation).privateMessage(event.isPrivate()).session(event.getSession()).triggerCount(currentTrigger.length()).build();
        return ctx;
    }
}
