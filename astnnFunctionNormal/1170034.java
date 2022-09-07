class BackupThread extends Thread {
    public Message duplicate(Destination to, Message in) throws JMSException {
        HermesConfig config = HermesBrowser.getBrowser().getConfig();
        Thread.currentThread().setContextClassLoader(contextClassLoader);
        Message out = null;
        if (in instanceof TextMessage) {
            out = createTextMessage(((TextMessage) in).getText());
        } else if (in instanceof ObjectMessage) {
            out = createObjectMessage();
            ((ObjectMessage) out).setObject(((ObjectMessage) in).getObject());
        } else if (in instanceof MapMessage) {
            out = createMapMessage();
            final MapMessage inMap = (MapMessage) in;
            final MapMessage outMap = (MapMessage) out;
            for (Enumeration iter = inMap.getMapNames(); iter.hasMoreElements(); ) {
                String key = (String) iter.nextElement();
                outMap.setObject(key, inMap.getObject(key));
            }
        } else if (in instanceof StreamMessage) {
            out = createStreamMessage();
            final StreamMessage inS = (StreamMessage) in;
            final StreamMessage outS = (StreamMessage) out;
            try {
                for (; ; ) {
                    outS.writeObject(inS.readObject());
                }
            } catch (MessageEOFException ex2) {
            }
        } else if (in instanceof BytesMessage) {
            out = createBytesMessage();
            final BytesMessage inBytes = (BytesMessage) in;
            final BytesMessage outBytes = (BytesMessage) out;
            inBytes.reset();
            try {
                outBytes.writeBytes(MessageUtils.asBytes(inBytes));
            } catch (MessageEOFException ex2) {
            }
        } else if (in instanceof Message) {
            out = createMessage();
        } else {
            throw new HermesException("Cannot handle " + in.getClass().getName());
        }
        try {
            out.setJMSDestination(to);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        if (config.isCopyJMSCorrelationID()) {
            try {
                out.setJMSCorrelationID(in.getJMSCorrelationID());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        if (config.isCopyJMSExpiration()) {
            try {
                out.setJMSExpiration(in.getJMSExpiration());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        if (config.isCopyJMSPriority()) {
            try {
                out.setJMSPriority(in.getJMSPriority());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        if (config.isCopyJMSReplyTo()) {
            try {
                if (in.getJMSReplyTo() != null) {
                    out.setJMSReplyTo(getDestination(getDestinationName(in.getJMSReplyTo()), Domain.getDomain(in.getJMSReplyTo())));
                }
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            } catch (NamingException ex) {
                throw new HermesRuntimeException(ex);
            }
        }
        if (config.isCopyJMSType()) {
            try {
                out.setJMSType(in.getJMSType());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        if (in.getPropertyNames() != null) {
            for (final Enumeration iter = in.getPropertyNames(); iter.hasMoreElements(); ) {
                final String key = (String) iter.nextElement();
                if (!key.startsWith("JMS") || config.isCopyJMSProviderProperties()) {
                    out.setObjectProperty(key, in.getObjectProperty(key));
                }
            }
        }
        return out;
    }
}
