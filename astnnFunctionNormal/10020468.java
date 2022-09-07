class BackupThread extends Thread {
    protected Collection<Element> getMetaLinkElements(final Page.Request pageRequest) throws WWWeeePortal.Exception {
        final ArrayList<Element> metaLinkElements = new ArrayList<Element>();
        CollectionUtil.addAll(metaLinkElements, ConfigManager.getConfigProps(definition.getProperties(), LINK_BY_NUM_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), MarkupManager.PROP_RESULT_LINK_ELEMENT_CONVERTER, true, true).values());
        CollectionUtil.addAll(metaLinkElements, CollectionUtil.values(ConfigManager.RegexPropKeyConverter.getMatchingValues(ConfigManager.getConfigProps(definition.getProperties(), LINK_BY_PATH_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), LINK_BY_PATH_CONVERTER, true, true), null, false, StringUtil.toString(pageRequest.getChannelLocalPath(this), null))));
        return (!metaLinkElements.isEmpty()) ? metaLinkElements : null;
    }
}
