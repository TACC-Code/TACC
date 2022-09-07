class BackupThread extends Thread {
    private static void appendBeanProperties(Object obj, Class type, String dateformat, Writer ret, String[] filters) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
        for (int n = 0; n < attributes.length; n++) {
            PropertyDescriptor propertyDescriptor = attributes[n];
            String attrName = propertyDescriptor.getName();
            if (attrName.equals("class")) continue; else if (isexclusive(attrName, filters)) {
                continue;
            }
            Class ptype = propertyDescriptor.getPropertyType();
            Method readmethod = propertyDescriptor.getReadMethod();
            Method writermethod = propertyDescriptor.getWriteMethod();
            if (readmethod == null || writermethod == null) continue;
            try {
                Object value = readmethod.invoke(obj);
                if (value != null) ptype = value.getClass();
                convertBeanObjectToXML(attrName, value, ptype, dateformat, ret);
            } catch (IllegalArgumentException e) {
                throw new CurrentlyInCreationException("", e);
            } catch (IllegalAccessException e) {
                throw new CurrentlyInCreationException("", e);
            } catch (InvocationTargetException e) {
                throw new CurrentlyInCreationException("", e);
            } catch (CurrentlyInCreationException e) {
                throw e;
            } catch (BeanInstanceException e) {
                throw e;
            } catch (Exception e) {
                throw new CurrentlyInCreationException("", e);
            }
        }
    }
}
