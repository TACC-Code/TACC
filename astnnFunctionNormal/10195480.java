class BackupThread extends Thread {
        public void notRewritten(AJOIdentifier jobId) {
            try {
                XKnownAction xka = getXKnownAction(jobId);
                xka.setFinishedRewriting();
                if (xka.getStatus().isEquivalent(AbstractActionStatus.READY)) {
                    kadb.ready(xka);
                } else if (xka.getStatus().isEquivalent(AbstractActionStatus.HELD)) {
                    xka.logEvent("AJO rewriter returned (not rewritten) while HELD, will wait for RESUME");
                } else if (xka.getStatus().isEquivalent(AbstractActionStatus.DONE)) {
                    xka.logEvent("AJO rewriter returned (not rewritten), but already DONE. Ignore it.");
                } else {
                    xka.logEvent("AJO rewriter returned (not rewritten) while in some unexpected state,. Ignore it.");
                }
            } catch (NJSException e) {
                logger.info("Error executing notRewritten for AJO re writer on <" + jobId.getName() + "/" + Integer.toHexString(jobId.getValue()) + ">. Message: " + e.getMessage());
            }
        }
}
