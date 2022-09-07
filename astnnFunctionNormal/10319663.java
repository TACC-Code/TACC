class BackupThread extends Thread {
    public static Element getDomElement(Document document, DomNode node, VapScheme scheme) {
        try {
            Logger log = DasLogger.getLogger(DasLogger.SYSTEM_LOG);
            String elementName = scheme.getName(node.getClass());
            DomNode defl = node.getClass().newInstance();
            Element element = null;
            element = document.createElement(elementName);
            BeanInfo info = BeansUtil.getBeanInfo(node.getClass());
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            for (int i = 0; i < properties.length; i++) {
                PropertyDescriptor pd = properties[i];
                String propertyName = pd.getName();
                if (propertyName.equals("class")) continue;
                if (propertyName.equals("controller")) {
                    continue;
                }
                log.fine("serializing property \"" + propertyName + "\" of " + elementName + " id=" + node.getId());
                Method readMethod = pd.getReadMethod();
                Method writeMethod = pd.getWriteMethod();
                if (writeMethod == null || readMethod == null) {
                    log.info("skipping property \"" + propertyName + "\" of " + elementName + ", failed to find read and write method.");
                    continue;
                }
                Object value = null;
                try {
                    value = readMethod.invoke(node, new Object[0]);
                } catch (IllegalAccessException ex) {
                    log.log(Level.SEVERE, null, ex);
                    continue;
                } catch (IllegalArgumentException ex) {
                    log.log(Level.SEVERE, null, ex);
                    continue;
                } catch (InvocationTargetException ex) {
                    log.log(Level.SEVERE, null, ex);
                    continue;
                }
                if (value == null) {
                    log.info("skipping property " + propertyName + " of " + elementName + ", value is null.");
                    continue;
                }
                if (propertyName.equals("id") && ((String) value).length() > 0) {
                    element.setAttribute(propertyName, (String) value);
                    continue;
                }
                IndexedPropertyDescriptor ipd = null;
                if (pd instanceof IndexedPropertyDescriptor) {
                    ipd = (IndexedPropertyDescriptor) pd;
                }
                if (value instanceof DomNode) {
                    Element propertyElement = document.createElement("property");
                    propertyElement.setAttribute("name", propertyName);
                    propertyElement.setAttribute("type", "DomNode");
                    Element child = getDomElement(document, (DomNode) value, scheme);
                    propertyElement.appendChild(child);
                    element.appendChild(propertyElement);
                } else if (ipd != null && (DomNode.class.isAssignableFrom(ipd.getIndexedPropertyType()))) {
                    Element propertyElement = document.createElement("property");
                    propertyElement.setAttribute("name", propertyName);
                    String clasName = scheme.getName(ipd.getIndexedPropertyType());
                    propertyElement.setAttribute("class", clasName);
                    propertyElement.setAttribute("length", String.valueOf(Array.getLength(value)));
                    for (int j = 0; j < Array.getLength(value); j++) {
                        Object value1 = Array.get(value, j);
                        Element child = getDomElement(document, (DomNode) value1, scheme);
                        propertyElement.appendChild(child);
                    }
                    element.appendChild(propertyElement);
                } else if (ipd != null) {
                    Element propertyElement = document.createElement("property");
                    propertyElement.setAttribute("name", propertyName);
                    String clasName = scheme.getName(ipd.getIndexedPropertyType());
                    propertyElement.setAttribute("class", clasName);
                    propertyElement.setAttribute("length", String.valueOf(Array.getLength(value)));
                    for (int j = 0; j < Array.getLength(value); j++) {
                        Object value1 = Array.get(value, j);
                        Element child = getElementForLeafNode(document, ipd.getIndexedPropertyType(), value1, null);
                        propertyElement.appendChild(child);
                    }
                    element.appendChild(propertyElement);
                } else {
                    Object defltValue = DomUtil.getPropertyValue(defl, pd.getName());
                    Element prop = getElementForLeafNode(document, pd.getPropertyType(), value, defltValue);
                    if (prop == null) {
                        log.warning("unable to serialize " + propertyName);
                        continue;
                    }
                    prop.setAttribute("name", pd.getName());
                    element.appendChild(prop);
                }
            }
            return element;
        } catch (IntrospectionException ex) {
            Logger.getLogger(SerializeUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SerializeUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(SerializeUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SerializeUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SerializeUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
