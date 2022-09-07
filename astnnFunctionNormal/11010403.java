class BackupThread extends Thread {
    public Map<String, Object> getParams(NodeChannelControl control) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CHANNEL_ID", control.getChannelId());
        params.put("IGNORE_ENABLED", control.isIgnoreEnabled());
        params.put("LAST_EXTRACT_TIME", control.getLastExtractTime());
        params.put("NODE_ID", control.getNodeId());
        params.put("SUSPEND_ENABLED", control.isSuspendEnabled());
        return params;
    }
}
