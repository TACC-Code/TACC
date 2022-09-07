class BackupThread extends Thread {
    protected Collection<Element> getMetaMetaElements(final Page.Request pageRequest) throws WWWeeePortal.Exception {
        final ArrayList<Element> metaMetaElements = new ArrayList<Element>();
        CollectionUtil.addAll(metaMetaElements, ConfigManager.getConfigProps(definition.getProperties(), META_BY_NUM_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), MarkupManager.PROP_RESULT_META_ELEMENT_CONVERTER, true, true).values());
        CollectionUtil.addAll(metaMetaElements, CollectionUtil.values(ConfigManager.RegexPropKeyConverter.getMatchingValues(ConfigManager.getConfigProps(definition.getProperties(), META_BY_PATH_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), META_BY_PATH_CONVERTER, true, true), null, false, StringUtil.toString(pageRequest.getChannelLocalPath(this), null))));
        return (!metaMetaElements.isEmpty()) ? metaMetaElements : null;
    }
}
