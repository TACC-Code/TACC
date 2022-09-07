class BackupThread extends Thread {
    public JSONObject authorize(PubSubMessage message) {
        JSONObject result = null;
        if (message.getChannelName().equalsIgnoreCase(XMPP.WORKLOAD_CHANNEL_NAME)) {
            try {
                result = new JSONObject(message.getPayload());
                String[] params = result.getString(PAYLOAD).split(":");
                if (params == null || params.length == 0 || !uuid.equals(params[0])) {
                    return null;
                }
            } catch (JSONException e) {
                result = null;
                logger.error(e);
            }
        }
        return result;
    }
}
