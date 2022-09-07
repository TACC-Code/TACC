class BackupThread extends Thread {
    public Set<Attribute<T, Object>> extractAttributes(boolean read, boolean write) {
        Set<String> propertyNames = new LinkedHashSet<String>();
        Set<Attribute<T, Object>> attributes = new LinkedHashSet<Attribute<T, Object>>();
        Method[] allMethods = this.clAna.getMethods();
        for (Method oneMethod : allMethods) {
            String propertyName = ReflectUtils.extractPropertyName(oneMethod);
            if (propertyName == null) {
                continue;
            }
            if (propertyNames.contains(propertyName)) {
                continue;
            }
            try {
                Attribute<T, Object> bridge = this.createAttribute(oneMethod, read, write);
                if (bridge != null) {
                    attributes.add(bridge);
                    propertyNames.add(propertyName);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de la propriété " + propertyName + " du plugin " + getUserPluginValue(this.clAna));
            }
        }
        return attributes;
    }
}
