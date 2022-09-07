class BackupThread extends Thread {
    public void transformDocument(NodeInfo startNode, Result result) throws TransformerException {
        if (executable == null) {
            throw new XPathException("Stylesheet has not been compiled");
        }
        if (getMessageEmitter() == null) {
            Receiver me = makeMessageEmitter();
            setMessageEmitter(me);
            if (me instanceof Emitter && ((Emitter) me).getWriter() == null) {
                try {
                    ((Emitter) me).setWriter(new OutputStreamWriter(System.err));
                } catch (Exception err) {
                    try {
                        ((Emitter) me).setWriter(new OutputStreamWriter(System.err, "utf8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new XPathException(e);
                    }
                }
            }
        }
        getMessageEmitter().open();
        boolean mustClose = (result instanceof StreamResult && ((StreamResult) result).getOutputStream() == null);
        principalResult = result;
        if (principalResultURI == null) {
            principalResultURI = result.getSystemId();
        }
        XPathContextMajor initialContext = newXPathContext();
        initialContext.setOriginatingConstructType(Location.CONTROLLER);
        if (startNode != null) {
            initialContextItem = startNode;
            contextForGlobalVariables = startNode.getRoot();
            if (startNode.getConfiguration() == null) {
                throw new TransformerException("The supplied source document must be associated with a Configuration");
            }
            if (!startNode.getConfiguration().isCompatible(preparedStylesheet.getConfiguration())) {
                throw new XPathException("Source document and stylesheet must use the same or compatible Configurations", SaxonErrorCode.SXXP0004);
            }
            SequenceIterator currentIter = SingletonIterator.makeIterator(startNode);
            if (initialTemplate != null) {
                currentIter.next();
            }
            initialContext.setCurrentIterator(currentIter);
        }
        initializeController();
        if (traceListener != null) {
            preEvaluateGlobals(initialContext);
        }
        Properties xslOutputProps;
        if (localOutputProperties == null) {
            xslOutputProps = executable.getDefaultOutputProperties();
        } else {
            xslOutputProps = localOutputProperties;
        }
        String nextInChain = xslOutputProps.getProperty(SaxonOutputKeys.NEXT_IN_CHAIN);
        if (nextInChain != null) {
            String baseURI = xslOutputProps.getProperty(SaxonOutputKeys.NEXT_IN_CHAIN_BASE_URI);
            result = prepareNextStylesheet(nextInChain, baseURI, result);
        }
        Properties props = new Properties(xslOutputProps);
        props.setProperty(SaxonOutputKeys.IMPLICIT_RESULT_DOCUMENT, "yes");
        initialContext.changeOutputDestination(props, result, true, Configuration.XSLT, Validation.PRESERVE, null);
        if (initialTemplate == null) {
            initialContextItem = startNode;
            final Mode mode = getRuleManager().getMode(initialMode, false);
            if (mode == null || (initialMode != null && mode.isEmpty())) {
                throw new XPathException("Requested initial mode " + (initialMode == null ? "" : initialMode.getDisplayName()) + " does not exist", "XTDE0045");
            }
            TailCall tc = ApplyTemplates.applyTemplates(initialContext.getCurrentIterator(), mode, null, null, initialContext, false, 0);
            while (tc != null) {
                tc = tc.processLeavingTail();
            }
        } else {
            Template t = initialTemplate;
            XPathContextMajor c2 = initialContext.newContext();
            initialContext.setOriginatingConstructType(Location.CONTROLLER);
            c2.openStackFrame(t.getStackFrameMap());
            c2.setLocalParameters(new ParameterSet());
            c2.setTunnelParameters(new ParameterSet());
            TailCall tc = t.expand(c2);
            while (tc != null) {
                tc = tc.processLeavingTail();
            }
        }
        if (traceListener != null) {
            traceListener.close();
        }
        Receiver out = initialContext.getReceiver();
        if (out instanceof ComplexContentOutputter && ((ComplexContentOutputter) out).contentHasBeenWritten()) {
            if (principalResultURI != null) {
                if (!checkUniqueOutputDestination(principalResultURI)) {
                    XPathException err = new XPathException("Cannot write more than one result document to the same URI, or write to a URI that has been read: " + result.getSystemId());
                    err.setErrorCode("XTDE1490");
                    throw err;
                } else {
                    addUnavailableOutputDestination(principalResultURI);
                }
            }
        }
        out.endDocument();
        out.close();
        getMessageEmitter().close();
        if (mustClose && result instanceof StreamResult) {
            OutputStream os = ((StreamResult) result).getOutputStream();
            if (os != null) {
                try {
                    os.close();
                } catch (java.io.IOException err) {
                    throw new XPathException(err);
                }
            }
        }
    }
}
