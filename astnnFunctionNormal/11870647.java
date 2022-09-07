class BackupThread extends Thread {
    protected Properties getConnectorProperties(Extension connectorExtension, File webserverCredentialDir, File webserverDeploymentDir) throws IOException {
        Properties props = new Properties();
        PluginDescriptor pluginDescriptor = connectorExtension.getDeclaringPluginDescriptor();
        String evaluatedProperty = null;
        if (connectorExtension.getParameter("port") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("port").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("port", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("address") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("address").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("address", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("maxThreads") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("maxThreads").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("maxThreads", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("maxHttpHeaderSize") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("maxHttpHeaderSize").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("maxHttpHeaderSize", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("emptySessionPath") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("emptySessionPath").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("emptySessionPath", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("protocol") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("protocol").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("protocol", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("enableLookups") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("enableLookups").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("enableLookups", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("redirectPort") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("redirectPort").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("redirectPort", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("acceptCount") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("acceptCount").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("acceptCount", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("connectionTimeout") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("connectionTimeout").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("connectionTimeout", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("disableUploadTimeout") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("disableUploadTimeout").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("disableUploadTimeout", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("SSLEnabled") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("SSLEnabled").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("SSLEnabled", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("scheme") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("scheme").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("scheme", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("secure") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("secure").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("secure", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("clientAuth") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("clientAuth").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("clientAuth", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("sslProtocol") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("sslProtocol").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("sslProtocol", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("keystoreFile") != null) {
            PluginDescriptor connectorPluginDescriptor = connectorExtension.getDeclaringPluginDescriptor();
            String sourceKeyStoreFilename = (String) evaluate(connectorExtension.getParameter("keystoreFile").valueAsString(), pluginDescriptor);
            if (sourceKeyStoreFilename != null) {
                File sourceKeyStoreFile = null;
                if (new File(sourceKeyStoreFilename).getPath().equals(new File(sourceKeyStoreFilename).getAbsolutePath())) {
                    sourceKeyStoreFile = new File(sourceKeyStoreFilename);
                } else {
                    sourceKeyStoreFile = getFilePath(connectorPluginDescriptor, sourceKeyStoreFilename);
                }
                logger.debug("Copy " + sourceKeyStoreFile.getPath() + " to " + webserverCredentialDir.getPath());
                FileUtils.copyFileToDirectory(sourceKeyStoreFile, webserverCredentialDir);
                File finalDeployedKeyStoreFile = new File(webserverDeploymentDir, sourceKeyStoreFile.getName());
                props.put("keystoreFile", escapeCurlyBraces(finalDeployedKeyStoreFile.getPath().replace("\\", "/")));
            }
        }
        if (connectorExtension.getParameter("keystoreType") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("keystoreType").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("keystoreType", escapeCurlyBraces(evaluatedProperty));
            }
        }
        if (connectorExtension.getParameter("keystorePass") != null) {
            evaluatedProperty = (String) evaluate(connectorExtension.getParameter("keystorePass").valueAsString(), pluginDescriptor);
            if (evaluatedProperty != null) {
                props.put("keystorePass", escapeCurlyBraces(evaluatedProperty));
            }
        }
        return props;
    }
}
