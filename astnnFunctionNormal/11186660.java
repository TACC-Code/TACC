class BackupThread extends Thread {
    private void fillBean(SpringAdapter adapter, Object bean, Type type, String name) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        if (name == null) {
            adapter.startBean(bean.getClass());
        } else {
            adapter.startBean(name, bean.getClass());
        }
        Class<?> clazz = (Class<?>) type;
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        for (PropertyDescriptor propDesc : beanInfo.getPropertyDescriptors()) {
            Method readMethod = propDesc.getReadMethod();
            Method writeMethod = propDesc.getWriteMethod();
            if (readMethod == null || writeMethod == null) {
                continue;
            }
            Class<?> returnClazz = readMethod.getReturnType();
            Object value = readMethod.invoke(bean);
            if (returnClazz.isPrimitive()) {
                adapter.startProperty(propDesc.getName(), value.toString());
                adapter.endProperty();
            } else if (value == null) {
                continue;
            } else if (isSupportedBuiltin(returnClazz) || returnClazz.isEnum()) {
                adapter.startProperty(propDesc.getName(), value.toString());
                adapter.endProperty();
            } else if (Map.class.isAssignableFrom(returnClazz)) {
                adapter.startProperty(propDesc.getName());
                Type returnType = readMethod.getGenericReturnType();
                ParameterizedType pType = (ParameterizedType) returnType;
                Map<?, ?> mapValue = (Map<?, ?>) value;
                fillMap(adapter, mapValue, pType);
                adapter.endProperty();
            } else if (List.class.isAssignableFrom(returnClazz)) {
                List<?> listValue = (List<?>) value;
                Type returnType = readMethod.getGenericReturnType();
                ParameterizedType pType = (ParameterizedType) returnType;
                adapter.startProperty(propDesc.getName());
                fillList(adapter, listValue, pType);
                adapter.endProperty();
            } else if (Set.class.isAssignableFrom(returnClazz)) {
                Set<?> setValue = (Set<?>) value;
                Type returnType = readMethod.getGenericReturnType();
                ParameterizedType pType = (ParameterizedType) returnType;
                adapter.startProperty(propDesc.getName());
                fillSet(adapter, setValue, pType);
                adapter.endProperty();
            } else if (reversedMap.containsKey(value)) {
                adapter.startProperty(propDesc.getName(), reversedMap.get(value));
                adapter.endProperty();
            } else {
                adapter.startProperty(propDesc.getName());
                fillBean(adapter, value, value.getClass(), null);
                adapter.endProperty();
            }
        }
        adapter.endBean();
    }
}
