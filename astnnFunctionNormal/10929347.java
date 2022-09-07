class BackupThread extends Thread {
    protected void runApplication() throws NoMediaControlChannelException, InvalidSessionAddressException {
        try {
            sClient = this.getContext().getSpeechClient();
            tClient = this.getContext().getTelephonyClient();
            channelName = this.getContext().getPBXSession().getChannelName();
            sClient.turnOnBargeIn();
            String result = "";
            sClient.enableDtmf("[0-9]{4}", this, 0, 0);
            while (!stopFlag) {
                _logger.debug("Calling play and Recognize...");
                RecognitionResult r = sClient.playAndRecognizeBlocking(false, result + prompt, grammar, false);
                if ((r != null) && (!r.isOutOfGrammar())) {
                    _logger.debug("Got a result: " + r.getText());
                    String main = null;
                    String phoneType = null;
                    for (RuleMatch rule : r.getRuleMatches()) {
                        _logger.info(rule.getTag() + ":" + rule.getRule());
                        if (rule.getRule().equals("main")) {
                            main = rule.getTag();
                        } else if (rule.getRule().equals("phoneType")) {
                            phoneType = rule.getTag();
                        } else {
                        }
                    }
                    if (main != null) {
                        if (main.equals("QUIT")) {
                            stopFlag = true;
                            try {
                                this.getContext().dialogCompleted();
                            } catch (InvalidContextException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String employee = main;
                            if (phoneType != null) {
                                _logger.info("phonetype is sepecified and it is: " + phoneType);
                                employee = employee + "-" + phoneType;
                            }
                            tClient.redirectBlocking(channelName, pbxContext, employee);
                            stopFlag = true;
                        }
                    } else {
                        _logger.warn("Invalid results, could not intepret the utterance.");
                    }
                } else {
                    _logger.debug("No recognition result...");
                    result = "I did not understand.  ";
                }
            }
        } catch (MrcpInvocationException e) {
            _logger.info("MRCP Response status code is: " + e.getResponse().getStatusCode());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
