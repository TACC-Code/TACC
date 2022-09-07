class BackupThread extends Thread {
    public void testGetterSetters() throws Exception {
        ChannelProcessingFilter filter = new ChannelProcessingFilter();
        filter.setChannelDecisionManager(new MockChannelDecisionManager(false, "MOCK"));
        assertTrue(filter.getChannelDecisionManager() != null);
        ConfigAttributeDefinition attr = new ConfigAttributeDefinition();
        attr.addConfigAttribute(new SecurityConfig("MOCK"));
        MockFilterInvocationDefinitionMap fids = new MockFilterInvocationDefinitionMap("/path", attr, false);
        filter.setFilterInvocationDefinitionSource(fids);
        assertTrue(filter.getFilterInvocationDefinitionSource() != null);
        filter.init(null);
        filter.afterPropertiesSet();
        filter.destroy();
    }
}
