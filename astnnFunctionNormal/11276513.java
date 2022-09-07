class BackupThread extends Thread {
    protected PropertyType parseProperty(Object theBean, Class classBean, PropertyDescriptor pd) {
        Class pt = pd.getPropertyType();
        if (pt == null || (pd.getName().equals("class") && pt.equals(java.lang.Class.class)) || (onlyChangeableProperty && pd.getWriteMethod() == null)) return null; else {
            String propertyType = strj.ft("basic/constraint/" + pt.getName());
            if (propertyType == null) propertyType = pd.getPropertyType().getName();
            Method readMethod = pd.getReadMethod(), writeMethod = pd.getWriteMethod();
            String readName = null;
            StringBuffer bName = new StringBuffer(pd.getName());
            char c = bName.charAt(0);
            if (c >= 'a' && c <= 'z') bName.setCharAt(0, Character.toUpperCase(c));
            try {
                if (readMethod != null) readName = readMethod.getName(); else {
                    readName = "get" + bName;
                    readMethod = classBean.getMethod(readName, null);
                }
            } catch (NoSuchMethodException error) {
            }
            Object initial = null;
            if (readMethod != null) {
                try {
                    initial = readMethod.invoke(theBean, null);
                } catch (Exception error) {
                }
            }
            PropertyType propType = new PropertyType(pd.getName(), false, propertyType, (initial == null) ? null : initial.toString());
            propType.setVisibility(propertyVisibility);
            propType.setChangeable(writeMethod != null);
            return propType;
        }
    }
}
