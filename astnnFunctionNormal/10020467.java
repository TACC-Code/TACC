class BackupThread extends Thread {
    public Map<String, Object> getMetaProps(final Page.Request pageRequest, final Map<String, Object> metaProps) throws IllegalArgumentException, ConfigManager.ConfigException {
        if (metaProps == null) throw new IllegalArgumentException("null metaProps");
        definition.getMetaProps(pageRequest, metaProps, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), (definition instanceof ContentManager.LocalChannelDefinition) ? 2 : 1);
        metaProps.put("WWWeee.Request.ChannelLocalPath", pageRequest.getChannelLocalPath(this));
        metaProps.put("WWWeee.Channel.Maximized", Boolean.valueOf(pageRequest.isMaximized(this)));
        metaProps.put("WWWeee.Channel", this);
        return metaProps;
    }
}
