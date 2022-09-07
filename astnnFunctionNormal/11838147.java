class BackupThread extends Thread {
    public Map<String, Property> getProperties(java.lang.reflect.Type theType) {
        if (propertiesCache.containsKey(theType)) {
            return propertiesCache.get(theType);
        }
        final Map<String, Property> properties = new HashMap<String, Property>();
        Type<?> typeHolder;
        if (theType instanceof Type) {
            typeHolder = (Type<?>) theType;
        } else if (theType instanceof Class) {
            typeHolder = TypeFactory.valueOf((Class<?>) theType);
        } else {
            throw new IllegalArgumentException("type " + theType + " not supported.");
        }
        BeanInfo beanInfo;
        try {
            LinkedList<Class<? extends Object>> types = new LinkedList<Class<? extends Object>>();
            types.addFirst((Class<? extends Object>) typeHolder.getRawType());
            while (!types.isEmpty()) {
                Class<? extends Object> type = types.removeFirst();
                beanInfo = Introspector.getBeanInfo(type);
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                for (final PropertyDescriptor pd : descriptors) {
                    try {
                        final Property property = new Property();
                        Method readMethod;
                        if (pd.getReadMethod() == null && Boolean.class.equals(pd.getPropertyType())) {
                            try {
                                readMethod = type.getMethod("is" + pd.getName().substring(0, 1).toUpperCase() + pd.getName().substring(1));
                            } catch (NoSuchMethodException e) {
                                readMethod = null;
                            }
                        } else {
                            readMethod = pd.getReadMethod();
                        }
                        final Method writeMethod = pd.getWriteMethod();
                        property.setExpression(pd.getName());
                        property.setName(pd.getName());
                        if (readMethod != null) {
                            property.setGetter(readMethod.getName() + "()");
                        }
                        if (writeMethod != null) {
                            property.setSetter(writeMethod.getName() + "(%s)");
                        }
                        if (readMethod == null && writeMethod == null) {
                            continue;
                        }
                        Class<?> rawType = resolveRawPropertyType(pd);
                        if (typeHolder.isParameterized() || rawType.getTypeParameters().length > 0) {
                            Type<?> resolvedGenericType = null;
                            if (readMethod != null) {
                                resolvedGenericType = resolveGenericType(readMethod.getDeclaringClass().getDeclaredMethod(readMethod.getName(), new Class[0]).getGenericReturnType(), typeHolder);
                            }
                            if (resolvedGenericType != null && !resolvedGenericType.isAssignableFrom(rawType)) {
                                property.setType(resolvedGenericType);
                            } else {
                                property.setType(TypeFactory.valueOf(rawType));
                            }
                        } else {
                            property.setType(TypeFactory.valueOf(rawType));
                        }
                        Property existing = properties.get(pd.getName());
                        if (existing == null) {
                            properties.put(pd.getName(), property);
                        } else if (existing.getType().isAssignableFrom(property.getType()) && !existing.getType().equals(property.getType())) {
                            existing.setType(property.getType());
                        }
                    } catch (final Throwable e) {
                        e.printStackTrace();
                    }
                }
                if (type.getSuperclass() != null && !Object.class.equals(type.getSuperclass())) {
                    types.add(type.getSuperclass());
                }
                @SuppressWarnings("unchecked") List<? extends Class<? extends Object>> interfaces = Arrays.<Class<? extends Object>>asList(type.getInterfaces());
                types.addAll(interfaces);
            }
        } catch (final IntrospectionException e) {
            e.printStackTrace();
        }
        for (Field f : typeHolder.getRawType().getFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                final Property property = new Property();
                property.setExpression(f.getName());
                property.setName(f.getName());
                Class<?> rawType = f.getType();
                Type<?> genericType = resolveGenericType(f.getGenericType(), typeHolder);
                if (genericType != null && !genericType.isAssignableFrom(rawType)) {
                    property.setType(genericType);
                } else {
                    property.setType(TypeFactory.valueOf(rawType));
                }
                Property existing = properties.get(property.getName());
                if (existing == null) {
                    property.setGetter(property.getName());
                    property.setSetter(property.getName() + " = %s");
                    properties.put(property.getName(), property);
                }
            }
        }
        propertiesCache.put(theType, Collections.unmodifiableMap(properties));
        return properties;
    }
}
