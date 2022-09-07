class BackupThread extends Thread {
    public void start(PipelineContext context) {
        try {
            ExternalContext externalContext = (ExternalContext) context.getAttribute(PipelineContext.EXTERNAL_CONTEXT);
            Config config = (Config) readCacheInputAsObject(context, getInputByName(INPUT_CONFIG), new CacheableInputReader() {

                public Object read(org.orbeon.oxf.pipeline.api.PipelineContext context, ProcessorInput input) {
                    Config result = new Config();
                    Element configElement = readInputAsDOM4J(context, input).getRootElement();
                    result.login = configElement.element("login").getText();
                    result.password = configElement.element("password").getText();
                    Element onMessageReceivedElement = configElement.element("on-message-received");
                    if (onMessageReceivedElement != null) result.onMessageReceived = onMessageReceivedElement.getText();
                    return result;
                }
            });
            for (int sendMessageTry = 0; ; ) {
                sendMessageTry++;
                Session session;
                synchronized (loginToSession) {
                    session = (Session) loginToSession.get(config.login);
                    if (session == null) {
                        session = new Session(new DirectConnectionHandler());
                        if (config.onMessageReceived != null) {
                            PipelineProcessor onMessageReceived;
                            if (config.onMessageReceived.startsWith("#")) {
                                String inputName = config.onMessageReceived.substring(1);
                                final ProcessorInput input = getInputByName(inputName);
                                PipelineReader reader = new PipelineReader();
                                reader.createInput("pipeline").setOutput(new ProcessorImpl.ProcessorOutputImpl(getClass(), inputName) {

                                    protected void readImpl(org.orbeon.oxf.pipeline.api.PipelineContext context, ContentHandler contentHandler) {
                                        ((ProcessorImpl.ProcessorOutputImpl) input.getOutput()).readImpl(context, contentHandler);
                                    }

                                    protected Object getValidityImpl(org.orbeon.oxf.pipeline.api.PipelineContext context) {
                                        return ((ProcessorImpl.ProcessorOutputImpl) input.getOutput()).getValidityImpl(context);
                                    }

                                    protected OutputCacheKey getKeyImpl(org.orbeon.oxf.pipeline.api.PipelineContext context) {
                                        return ((ProcessorImpl.ProcessorOutputImpl) input.getOutput()).getKeyImpl(context);
                                    }
                                });
                                reader.start(context);
                                onMessageReceived = new PipelineProcessor(reader.getPipeline());
                            } else {
                                onMessageReceived = new PipelineProcessor();
                                URLGenerator urlGenerator = new URLGenerator(config.onMessageReceived);
                                PipelineUtils.connect(urlGenerator, "data", onMessageReceived, "config");
                            }
                            onMessageReceived.createInput("data");
                            session.addSessionListener(new SessionListener(onMessageReceived, externalContext));
                        }
                        session.login(config.login, config.password);
                        loginToSession.put(config.login, session);
                    }
                }
                Document dataDocument = readCacheInputAsDOM4J(context, INPUT_DATA);
                Element messageElement = dataDocument.getRootElement();
                try {
                    session.sendMessage(messageElement.element("to").getText(), messageElement.element("body").getText());
                } catch (IOException e) {
                    if (sendMessageTry < SEND_MESSAGE_MAX_TRY) {
                        synchronized (loginToSession) {
                            loginToSession.remove(config.login);
                        }
                        continue;
                    } else {
                        throw e;
                    }
                }
                break;
            }
        } catch (IOException e) {
            throw new OXFException(e);
        } catch (AccountLockedException e) {
            throw new OXFException(e);
        } catch (LoginRefusedException e) {
            throw new OXFException(e);
        }
    }
}
