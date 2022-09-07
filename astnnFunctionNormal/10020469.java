class BackupThread extends Thread {
    protected Collection<Element> getMetaScriptElements(final Page.Request pageRequest) throws WWWeeePortal.Exception {
        final ArrayList<Element> metaScriptElements = new ArrayList<Element>();
        CollectionUtil.addAll(metaScriptElements, ConfigManager.getConfigProps(definition.getProperties(), SCRIPT_BY_NUM_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), MarkupManager.PROP_RESULT_SCRIPT_ELEMENT_CONVERTER, true, true).values());
        CollectionUtil.addAll(metaScriptElements, CollectionUtil.values(ConfigManager.RegexPropKeyConverter.getMatchingValues(ConfigManager.getConfigProps(definition.getProperties(), SCRIPT_BY_PATH_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), SCRIPT_BY_PATH_CONVERTER, true, true), null, false, StringUtil.toString(pageRequest.getChannelLocalPath(this), null))));
        return (!metaScriptElements.isEmpty()) ? metaScriptElements : null;
    }
}
