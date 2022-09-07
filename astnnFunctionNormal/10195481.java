class BackupThread extends Thread {
        public void rewriteFailed(AJOIdentifier jobId, AJORewriterException cause) {
            try {
                XKnownAction xka = getXKnownAction(jobId);
                xka.setFinishedRewriting();
                if (xka.getStatus().isEquivalent(AbstractActionStatus.READY)) {
                    xka.setStatus(AbstractActionStatus.FAILED_IN_EXECUTION, "AJORewriter failed with the following reason: " + cause.getMessage());
                } else if (xka.getStatus().isEquivalent(AbstractActionStatus.HELD)) {
                    xka.setStatus(AbstractActionStatus.FAILED_IN_EXECUTION, "AJORewriter failed with the following reason: " + cause.getMessage());
                } else if (xka.getStatus().isEquivalent(AbstractActionStatus.DONE)) {
                    xka.logEvent("AJO rewriter returned (rewrite failed), but already DONE. Ignore it.");
                } else {
                    xka.setStatus(AbstractActionStatus.FAILED_IN_EXECUTION, "AJORewriter failed with the following reason: " + cause.getMessage());
                }
            } catch (NJSException e) {
                logger.info("Error executing rewriteFailed for AJO re writer on <" + jobId.getName() + "/" + Integer.toHexString(jobId.getValue()) + ">. Message: " + e.getMessage());
            }
        }
}
