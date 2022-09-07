class BackupThread extends Thread {
    private void getSimpleProperties(ExpressoComponent sourceComponent, ComponentMetadata metadata, ComponentConfig targetConfig) throws ConfigurationException {
        Map properties = metadata.getProperties();
        for (Iterator i = properties.keySet().iterator(); i.hasNext(); ) {
            Property property = (Property) i.next();
            String access = property.getAccess();
            if ("readwrite".equalsIgnoreCase(access) || "rw".equalsIgnoreCase(access)) {
                if (property instanceof com.jcorporate.expresso.kernel.metadata.SimpleProperty) {
                    String propertyName = property.getName();
                    try {
                        Object propertyValue = PropertyUtils.getProperty(sourceComponent, propertyName);
                        String stringValue = ConvertUtils.convert(propertyValue);
                        targetConfig.setProperty(propertyName, stringValue);
                    } catch (IllegalAccessException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Property " + propertyName + " specified in metadata was not accessible.  Must be 'public'", ex);
                    } catch (InvocationTargetException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Unable to get property specified in metadata:  " + propertyName, ex);
                    } catch (NoSuchMethodException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Getter method for property  " + propertyName + " specified in metadata does not exist", ex);
                    }
                } else if (property instanceof com.jcorporate.expresso.kernel.metadata.MappedProperty) {
                    MappedProperty mappedProperty = (MappedProperty) property;
                    String propertyName = property.getName();
                    Map allProperties = mappedProperty.getValues();
                    try {
                        for (Iterator j = allProperties.keySet().iterator(); j.hasNext(); ) {
                            String oneKey = (String) j.next();
                            Object propertyValue = PropertyUtils.getMappedProperty(sourceComponent, propertyName, oneKey);
                            String stringValue = ConvertUtils.convert(propertyValue);
                            targetConfig.setMappedProperty(propertyName, oneKey, stringValue);
                        }
                    } catch (IllegalAccessException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Property " + propertyName + " specified in metadata was not accessible.  Must be 'public'", ex);
                    } catch (InvocationTargetException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Unable to get property specified in metadata:  " + propertyName, ex);
                    } catch (NoSuchMethodException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Getter method for property  " + propertyName + " specified in metadata does not exist", ex);
                    }
                } else if (property instanceof com.jcorporate.expresso.kernel.metadata.IndexedProperty) {
                    IndexedProperty indexedProperty = (IndexedProperty) property;
                    String propertyName = property.getName();
                    Map allProperties = indexedProperty.getValues();
                    try {
                        for (Iterator j = allProperties.keySet().iterator(); j.hasNext(); ) {
                            Integer oneKey = (Integer) j.next();
                            Object propertyValue = PropertyUtils.getIndexedProperty(sourceComponent, propertyName, oneKey.intValue());
                            String stringValue = ConvertUtils.convert(propertyValue);
                            targetConfig.setIndexedProperty(propertyName, oneKey.intValue(), stringValue);
                        }
                    } catch (IllegalAccessException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Property " + propertyName + " specified in metadata was not accessible.  Must be 'public'", ex);
                    } catch (InvocationTargetException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Unable to get property specified in metadata:  " + propertyName, ex);
                    } catch (NoSuchMethodException ex) {
                        log.error("Error getting simple property ", ex);
                        throw new ConfigurationException("Getter method for property  " + propertyName + " specified in metadata does not exist", ex);
                    }
                }
            }
        }
    }
}
