class BackupThread extends Thread {
    public JSONServiceCall(ServiceCall bean) throws JSONException {
        super((Message) bean);
        this.put(PROP_DRIVER, stringFillHelper(bean.getDriver()));
        this.put(PROP_SERVICE, stringFillHelper(bean.getService()));
        this.put(PROP_PARAMETERS, bean.getParameters());
        this.put(PROP_INSTANCE_ID, bean.getInstanceId());
        this.put(PROP_SERVICE_TYPE, bean.getServiceType());
        this.put(PROP_CHANNELS, bean.getChannels());
        this.put(PROP_CHANNEL_IDS, bean.getChannelIDs());
        this.put(PROP_CHANNEL_TYPE, bean.getChannelType());
    }
}
