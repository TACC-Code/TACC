class BackupThread extends Thread {
        public void rewritten(AbstractJob rewrittenAJO) throws NJSException {
            XKnownAction xka = getXKnownAction(rewrittenAJO.getAJOId());
            xka.setFinishedRewriting();
            if (xka.getStatus().isEquivalent(AbstractActionStatus.READY)) {
                xka.replaceJob(rewrittenAJO);
                kadb.ready(xka);
            } else if (xka.getStatus().isEquivalent(AbstractActionStatus.HELD)) {
                xka.replaceJob(rewrittenAJO);
                xka.logEvent("AJO rewriter returned (rewritten) while HELD, will wait for RESUME");
            } else if (xka.getStatus().isEquivalent(AbstractActionStatus.DONE)) {
                xka.logEvent("AJO rewriter returned (rewritten), but already DONE. No action on XKA.");
                throw new NJSException("The target AJO is DONE (cancelled or aborted?). Rewritten AJO not executed.");
            } else {
                xka.logEvent("AJO rewriter returned (rewritten) while in some unexpected state. Ignore it.");
                throw new NJSException("The target AJO in in an unexpected state <" + xka.getStatus() + ">. Rewritten AJO not executed.");
            }
        }
}
