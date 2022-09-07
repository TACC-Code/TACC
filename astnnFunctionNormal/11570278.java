class BackupThread extends Thread {
    public static String build(LCOrder order) {
        try {
            JSONObject container = new JSONObject();
            List<JSONObject> fractionsJSON = new ArrayList<JSONObject>();
            JSONObject lfJSON = null;
            Collection<LCOrderItem> orderItems = order.getOrderItems();
            long now = new Date().getTime();
            for (LCOrderItem oi : orderItems) {
                Timestamp expirationTime = oi.getAppExpiration();
                lfJSON = new JSONObject();
                lfJSON.put(JSONKey.PURPOSE.toString(), oi.getLoanPurpose());
                lfJSON.put(JSONKey.LOANFRACTIONGUID.toString(), oi.getId());
                lfJSON.put(JSONKey.LOANFRACTIONAMOUNT.toString(), oi.getInvAmt());
                lfJSON.put(JSONKey.LOANTITLE.toString(), oi.getLoanTitle());
                lfJSON.put(JSONKey.LOANAMOUNTREQUESTED.toString(), oi.getLoanAmt());
                lfJSON.put(JSONKey.LOANRATE.toString(), format(100 * oi.getIntrestRate()));
                lfJSON.put(JSONKey.LOANUNFUNDEDAMOUNT.toString(), oi.getUnfundedAmt());
                lfJSON.put(JSONKey.LOANTIMEREMAINING.toString(), expirationTime.getTime() - now);
                lfJSON.put(JSONKey.LOANEXPIRATIONDATE.toString(), expirationTime);
                lfJSON.put(JSONKey.LOANGRADE.toString(), oi.getLoanClass().classGetClassName());
                lfJSON.put(JSONKey.LOANID.toString(), oi.getLoanId());
                lfJSON.put(JSONKey.UNCRUNCH.toString(), oi.getChannel().toString());
                lfJSON.put(JSONKey.LOANSTATUS.toString(), oi.getLoanStatus());
                lfJSON.put(JSONKey.LOANLENGTH.toString(), oi.getMaturity());
                lfJSON.put(JSONKey.ALREADYINVESTEDIN.toString(), oi.isAlreadyOwned());
                lfJSON.put(JSONKey.LOAN_TYPE.toString(), oi.getLoanType().getLabel());
                fractionsJSON.add(lfJSON);
            }
            container.put(JSONKey.LOANFRACTIONS.toString(), fractionsJSON);
            return container.toString();
        } catch (Exception e) {
            logger.warning("Error converting ORDER to JSON object: " + e);
            return "{}";
        }
    }
}
