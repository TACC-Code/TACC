class BackupThread extends Thread {
    private Map populateClassMapWithBeanInfo(Class clazz) {
        Map classMap = new HashMap();
        if (exposeFields) {
            Field[] fields = clazz.getFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    classMap.put(field.getName(), field);
                }
            }
        }
        Map accessibleMethods = discoverAccessibleMethods(clazz);
        Method genericGet = getFirstAccessibleMethod(MethodSignature.GET_STRING_SIGNATURE, accessibleMethods);
        if (genericGet == null) {
            genericGet = getFirstAccessibleMethod(MethodSignature.GET_OBJECT_SIGNATURE, accessibleMethods);
        }
        if (genericGet != null) {
            classMap.put(GENERIC_GET_KEY, genericGet);
        }
        if (exposureLevel == EXPOSE_NOTHING) {
            return classMap;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] pda = beanInfo.getPropertyDescriptors();
            MethodDescriptor[] mda = beanInfo.getMethodDescriptors();
            for (int i = pda.length - 1; i >= 0; --i) {
                PropertyDescriptor pd = pda[i];
                if (pd instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
                    Method readMethod = ipd.getIndexedReadMethod();
                    Method publicReadMethod = getAccessibleMethod(readMethod, accessibleMethods);
                    if (publicReadMethod != null && isSafeMethod(publicReadMethod)) {
                        try {
                            if (readMethod != publicReadMethod) {
                                ipd = new IndexedPropertyDescriptor(ipd.getName(), ipd.getReadMethod(), ipd.getWriteMethod(), publicReadMethod, ipd.getIndexedWriteMethod());
                            }
                            classMap.put(ipd.getName(), ipd);
                            getArgTypes(classMap).put(publicReadMethod, publicReadMethod.getParameterTypes());
                        } catch (IntrospectionException e) {
                            logger.warn("Failed creating a publicly-accessible " + "property descriptor for " + clazz.getName() + " indexed property " + pd.getName() + ", read method " + publicReadMethod + ", write method " + ipd.getIndexedWriteMethod(), e);
                        }
                    }
                } else {
                    Method readMethod = pd.getReadMethod();
                    Method publicReadMethod = getAccessibleMethod(readMethod, accessibleMethods);
                    if (publicReadMethod != null && isSafeMethod(publicReadMethod)) {
                        try {
                            if (readMethod != publicReadMethod) {
                                pd = new PropertyDescriptor(pd.getName(), publicReadMethod, pd.getWriteMethod());
                                pd.setReadMethod(publicReadMethod);
                            }
                            classMap.put(pd.getName(), pd);
                        } catch (IntrospectionException e) {
                            logger.warn("Failed creating a publicly-accessible " + "property descriptor for " + clazz.getName() + " property " + pd.getName() + ", read method " + publicReadMethod + ", write method " + pd.getWriteMethod(), e);
                        }
                    }
                }
            }
            if (exposureLevel < EXPOSE_PROPERTIES_ONLY) {
                for (int i = mda.length - 1; i >= 0; --i) {
                    MethodDescriptor md = mda[i];
                    Method method = md.getMethod();
                    Method publicMethod = getAccessibleMethod(method, accessibleMethods);
                    if (publicMethod != null && isSafeMethod(publicMethod)) {
                        String name = md.getName();
                        Object previous = classMap.get(name);
                        if (previous instanceof Method) {
                            MethodMap methodMap = new MethodMap(name, this);
                            methodMap.addMember((Method) previous);
                            methodMap.addMember(publicMethod);
                            classMap.put(name, methodMap);
                            getArgTypes(classMap).remove(previous);
                        } else if (previous instanceof MethodMap) {
                            ((MethodMap) previous).addMember(publicMethod);
                        } else {
                            classMap.put(name, publicMethod);
                            getArgTypes(classMap).put(publicMethod, publicMethod.getParameterTypes());
                        }
                    }
                }
            }
            return classMap;
        } catch (IntrospectionException e) {
            logger.warn("Couldn't properly perform introspection for class " + clazz, e);
            return new HashMap();
        }
    }
}
