class BackupThread extends Thread {
    private String codegenXmlInit(ServicesConfiguration config, String contextRoot, Map serviceImportMap) {
        StringBuffer e4x = new StringBuffer();
        String channelSetImplToImport = null;
        e4x.append("<services>\n");
        if (config.getDefaultChannels().size() > 0) {
            e4x.append("\t<default-channels>\n");
            for (Iterator chanIter = config.getDefaultChannels().iterator(); chanIter.hasNext(); ) {
                String id = (String) chanIter.next();
                e4x.append("\t\t<channel ref=\"" + id + "\"/>\n");
            }
            e4x.append("\t</default-channels>\n");
        }
        ClusterSettings defaultCluster = config.getDefaultCluster();
        if (defaultCluster != null && !defaultCluster.getURLLoadBalancing()) defaultCluster = null;
        for (Iterator servIter = config.getAllServiceSettings().iterator(); servIter.hasNext(); ) {
            ServiceSettings entry = (ServiceSettings) servIter.next();
            String serviceType = entry.getId();
            e4x.append("\t<service id=\"");
            e4x.append(serviceType);
            e4x.append("\"");
            e4x.append(">\n");
            String serviceClass = entry.getClassName();
            if (ADVANCED_MESSAGING_SUPPORT_CLASS.equals(serviceClass)) channelSetImplToImport = ADVANCED_CHANNELSET_CLASS;
            String useTransactionsStr = entry.getProperties().getPropertyAsString("use-transactions", null);
            if (useTransactionsStr != null) {
                e4x.append("\t\t<properties>\n\t\t\t<use-transactions>" + useTransactionsStr + "</use-transactions>\n");
                e4x.append("\t\t</properties>\n");
            }
            for (Iterator destIter = entry.getDestinationSettings().values().iterator(); destIter.hasNext(); ) {
                DestinationSettings dest = (DestinationSettings) destIter.next();
                String destination = dest.getId();
                e4x.append("\t\t<destination id=\"" + destination + "\">\n");
                ConfigMap metadata = dest.getProperties().getPropertyAsMap("metadata", null);
                boolean closePropTag = false;
                if (metadata != null) {
                    e4x.append("\t\t\t<properties>\n\t\t\t\t<metadata\n");
                    String extendsStr = metadata.getPropertyAsString("extends", null);
                    if (extendsStr != null) {
                        e4x.append(" extends=\"");
                        e4x.append(extendsStr);
                        e4x.append("\"");
                    }
                    e4x.append(">");
                    closePropTag = true;
                    List identities = metadata.getPropertyAsList("identity", null);
                    if (identities != null) {
                        Iterator it = identities.iterator();
                        while (it.hasNext()) {
                            Object o = it.next();
                            String identityName = null;
                            String undefinedValue = null;
                            if (o instanceof String) {
                                identityName = (String) o;
                            } else if (o instanceof ConfigMap) {
                                identityName = ((ConfigMap) o).getPropertyAsString("property", null);
                                undefinedValue = ((ConfigMap) o).getPropertyAsString("undefined-value", null);
                            }
                            if (identityName != null) {
                                e4x.append("\t\t\t\t\t<identity property=\"");
                                e4x.append(identityName);
                                e4x.append("\"");
                                if (undefinedValue != null) {
                                    e4x.append(" undefined-value=\"");
                                    e4x.append(undefinedValue);
                                    e4x.append("\"");
                                }
                                e4x.append("/>\n");
                            }
                        }
                    }
                    codegenServiceAssociations(metadata, e4x, destination, "one-to-many");
                    codegenServiceAssociations(metadata, e4x, destination, "many-to-many");
                    codegenServiceAssociations(metadata, e4x, destination, "one-to-one");
                    codegenServiceAssociations(metadata, e4x, destination, "many-to-one");
                    e4x.append("\t\t\t\t</metadata>\n");
                }
                String itemClass = dest.getProperties().getPropertyAsString("item-class", null);
                if (itemClass != null) {
                    if (!closePropTag) {
                        e4x.append("\t\t\t<properties>\n");
                        closePropTag = true;
                    }
                    e4x.append("\t\t\t\t<item-class>");
                    e4x.append(itemClass);
                    e4x.append("</item-class>\n");
                }
                ConfigMap network = dest.getProperties().getPropertyAsMap("network", null);
                ConfigMap clusterInfo = null;
                ConfigMap pagingInfo = null;
                ConfigMap reconnectInfo = null;
                if (network != null || defaultCluster != null) {
                    if (!closePropTag) {
                        e4x.append("\t\t\t<properties>\n");
                        closePropTag = true;
                    }
                    e4x.append("\t\t\t\t<network>\n");
                    if (network != null) pagingInfo = network.getPropertyAsMap("paging", null);
                    if (pagingInfo != null) {
                        String enabled = pagingInfo.getPropertyAsString("enabled", "false");
                        e4x.append("\t\t\t\t\t<paging enabled=\"");
                        e4x.append(enabled);
                        e4x.append("\"");
                        String size = pagingInfo.getPropertyAsString("page-size", pagingInfo.getPropertyAsString("pageSize", null));
                        if (size != null) {
                            e4x.append(" page-size=\"");
                            e4x.append(size);
                            e4x.append("\"");
                            e4x.append(" pageSize=\"");
                            e4x.append(size);
                            e4x.append("\"");
                        }
                        e4x.append("/>\n");
                    }
                    if (network != null) reconnectInfo = network.getPropertyAsMap("reconnect", null);
                    if (reconnectInfo != null) {
                        String fetchOption = reconnectInfo.getPropertyAsString("fetch", "IDENTITY");
                        e4x.append("\t\t\t\t\t<reconnect fetch=\"");
                        e4x.append(fetchOption.toUpperCase());
                        e4x.append("\" />\n");
                    }
                    if (network != null) {
                        String reliable = network.getPropertyAsString("reliable", "false");
                        if (Boolean.valueOf(reliable).booleanValue()) {
                            e4x.append("\t\t\t\t\t<reliable>");
                            e4x.append(reliable);
                            e4x.append("</reliable>\n");
                        }
                    }
                    if (network != null) clusterInfo = network.getPropertyAsMap("cluster", null);
                    if (clusterInfo != null) {
                        String clusterId = clusterInfo.getPropertyAsString("ref", null);
                        ClusterSettings clusterSettings = config.getClusterSettings(clusterId);
                        if (clusterSettings != null && clusterSettings.getURLLoadBalancing()) {
                            e4x.append("\t\t\t\t\t<cluster ref=\"");
                            e4x.append(clusterId);
                            e4x.append("\"/>\n");
                        }
                    } else if (defaultCluster != null) {
                        e4x.append("\t\t\t\t\t<cluster");
                        if (defaultCluster.getClusterName() != null) {
                            e4x.append(" ref=\"");
                            e4x.append(defaultCluster.getClusterName());
                            e4x.append("\"");
                        }
                        e4x.append("/>\n");
                    }
                    e4x.append("\t\t\t\t</network>\n");
                }
                String useTransactions = dest.getProperties().getPropertyAsString("use-transactions", null);
                if (useTransactions != null) {
                    if (!closePropTag) {
                        e4x.append("\t\t\t<properties>\n");
                        closePropTag = true;
                    }
                    e4x.append("\t\t\t\t<use-transactions>" + useTransactions + "</use-transactions>\n");
                }
                String autoSyncEnabled = dest.getProperties().getPropertyAsString("auto-sync-enabled", "true");
                if (autoSyncEnabled.equalsIgnoreCase("false")) {
                    if (!closePropTag) {
                        e4x.append("\t\t\t<properties>\n");
                        closePropTag = true;
                    }
                    e4x.append("\t\t\t\t<auto-sync-enabled>false</auto-sync-enabled>\n");
                }
                if (closePropTag) {
                    e4x.append("\t\t\t</properties>\n");
                }
                e4x.append("\t\t\t<channels>\n");
                for (Iterator chanIter = dest.getChannelSettings().iterator(); chanIter.hasNext(); ) {
                    e4x.append("\t\t\t\t<channel ref=\"" + ((ChannelSettings) chanIter.next()).getId() + "\"/>\n");
                }
                e4x.append("\t\t\t</channels>\n");
                e4x.append("\t\t</destination>\n");
            }
            e4x.append("\t</service>\n");
        }
        e4x.append("\t<channels>\n");
        String channelType;
        for (Iterator chanIter = config.getAllChannelSettings().values().iterator(); chanIter.hasNext(); ) {
            ChannelSettings chan = (ChannelSettings) chanIter.next();
            if (chan.getServerOnly()) continue;
            channelType = chan.getClientType();
            serviceImportMap.put(channelType, channelType);
            e4x.append("\t\t<channel id=\"" + chan.getId() + "\" type=\"" + channelType + "\">\n");
            StringBuffer channelProps = new StringBuffer();
            containsClientLoadBalancing = false;
            channelProperties(chan.getProperties(), channelProps, "\t\t\t\t");
            if (!containsClientLoadBalancing) e4x.append("\t\t\t<endpoint uri=\"" + chan.getClientParsedUri(contextRoot) + "\"/>\n");
            containsClientLoadBalancing = false;
            e4x.append("\t\t\t<properties>\n");
            e4x.append(channelProps);
            e4x.append("\t\t\t</properties>\n");
            e4x.append("\t\t</channel>\n");
        }
        e4x.append("\t</channels>\n");
        FlexClientSettings flexClientSettings = (config instanceof ClientConfiguration) ? ((ClientConfiguration) config).getFlexClientSettings() : null;
        if (flexClientSettings != null && flexClientSettings.getHeartbeatIntervalMillis() > 0) {
            e4x.append("\t<flex-client>\n");
            e4x.append("\t\t<heartbeat-interval-millis>");
            e4x.append(flexClientSettings.getHeartbeatIntervalMillis());
            e4x.append("</heartbeat-interval-millis>");
            e4x.append("\t</flex-client>\n");
        }
        e4x.append("</services>");
        StringBuffer advancedMessagingSupport = new StringBuffer();
        if (channelSetImplToImport != null) {
            serviceImportMap.put("ChannelSetImpl", channelSetImplToImport);
            String alias = "flex.messaging.messages.ReliabilityMessage";
            String className = "mx.messaging.messages.ReliabilityMessage";
            advancedMessagingSupport.append("     ServerConfig.channelSetFactory = AdvancedChannelSet;\n");
            advancedMessagingSupport.append("     try {\n");
            advancedMessagingSupport.append("     if (flash.net.getClassByAlias(\"" + alias + "\") == null){\n");
            advancedMessagingSupport.append("         flash.net.registerClassAlias(\"" + alias + "\", " + className + ");}\n");
            advancedMessagingSupport.append("     } catch (e:Error) {\n");
            advancedMessagingSupport.append("         flash.net.registerClassAlias(\"" + alias + "\", " + className + "); }\n");
            if (flexClientSettings != null && flexClientSettings.getReliableReconnectDurationMillis() > 0) {
                advancedMessagingSupport.append("     AdvancedChannelSet.reliableReconnectDuration =");
                advancedMessagingSupport.append(flexClientSettings.getReliableReconnectDurationMillis());
                advancedMessagingSupport.append(";\n");
            }
        }
        String generatedChunk = "\n     ServerConfig.xml =\n" + e4x.toString() + ";\n" + advancedMessagingSupport.toString();
        return generatedChunk;
    }
}
