class BackupThread extends Thread {
    public ComponentContextImpl(ComponentRegistryImpl componentRegistry, ComponentWrapper component, Map<String, ?> properties) {
        super(componentRegistry);
        this.component = component;
        this.properties = properties;
        this.queue = new ArrayBlockingQueue<Exchange>(DEFAULT_QUEUE_CAPACITY);
        this.componentEndpoint = new EndpointImpl(properties);
        this.componentEndpoint.setQueue(queue);
        this.componentRegistry.getNmr().getEndpointRegistry().register(componentEndpoint, properties);
        this.dc = new DeliveryChannelImpl(this, componentEndpoint.getChannel(), queue);
        this.name = (String) properties.get(ComponentRegistry.NAME);
        this.workspaceRoot = new File(System.getProperty("karaf.base"), System.getProperty("jbi.cache", "data/jbi/") + name + "/workspace");
        this.workspaceRoot.mkdirs();
        this.installRoot = new File(System.getProperty("karaf.base"), System.getProperty("jbi.cache", "data/jbi/") + name + "/install");
        this.installRoot.mkdirs();
    }
}
