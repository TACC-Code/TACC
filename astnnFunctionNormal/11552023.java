class BackupThread extends Thread {
    public Object doUnmarshal(final Object result, final HierarchicalStreamReader reader, final UnmarshallingContext context) {
        final Class[] currentType = new Class[1];
        if (!ATTRIBUTE_VALUE_CUSTOM.equals(reader.getAttribute(mapper.aliasForAttribute(ATTRIBUTE_SERIALIZATION)))) {
            throw new ConversionException("Cannot deserialize object with new readObject()/writeObject() methods");
        }
        CustomObjectInputStream.StreamCallback callback = new CustomObjectInputStream.StreamCallback() {

            public Object readFromStream() {
                reader.moveDown();
                Class type = mapper.realClass(reader.getNodeName());
                Object value = context.convertAnother(result, type);
                reader.moveUp();
                return value;
            }

            public Map readFieldsFromStream() {
                final Map fields = new HashMap();
                reader.moveDown();
                if (reader.getNodeName().equals(ELEMENT_FIELDS)) {
                    while (reader.hasMoreChildren()) {
                        reader.moveDown();
                        if (!reader.getNodeName().equals(ELEMENT_FIELD)) {
                            throw new ConversionException("Expected <" + ELEMENT_FIELD + "/> element inside <" + ELEMENT_FIELD + "/>");
                        }
                        String name = reader.getAttribute(ATTRIBUTE_NAME);
                        Class type = mapper.realClass(reader.getAttribute(ATTRIBUTE_CLASS));
                        Object value = context.convertAnother(result, type);
                        fields.put(name, value);
                        reader.moveUp();
                    }
                } else if (reader.getNodeName().equals(ELEMENT_DEFAULT)) {
                    ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(currentType[0]);
                    while (reader.hasMoreChildren()) {
                        reader.moveDown();
                        String name = mapper.realMember(currentType[0], reader.getNodeName());
                        if (mapper.shouldSerializeMember(currentType[0], name)) {
                            String typeName = reader.getAttribute(mapper.aliasForAttribute(ATTRIBUTE_CLASS));
                            Class type;
                            if (typeName != null) {
                                type = mapper.realClass(typeName);
                            } else {
                                ObjectStreamField field = objectStreamClass.getField(name);
                                if (field == null) {
                                    throw new ObjectAccessException("Class " + currentType[0] + " does not contain a field named '" + name + "'");
                                }
                                type = field.getType();
                            }
                            Object value = context.convertAnother(result, type);
                            fields.put(name, value);
                        }
                        reader.moveUp();
                    }
                } else {
                    throw new ConversionException("Expected <" + ELEMENT_FIELDS + "/> or <" + ELEMENT_DEFAULT + "/> element when calling ObjectInputStream.readFields()");
                }
                reader.moveUp();
                return fields;
            }

            public void defaultReadObject() {
                if (!reader.hasMoreChildren()) {
                    return;
                }
                reader.moveDown();
                if (!reader.getNodeName().equals(ELEMENT_DEFAULT)) {
                    throw new ConversionException("Expected <" + ELEMENT_DEFAULT + "/> element in readObject() stream");
                }
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    String fieldName = mapper.realMember(currentType[0], reader.getNodeName());
                    if (mapper.shouldSerializeMember(currentType[0], fieldName)) {
                        String classAttribute = reader.getAttribute(mapper.aliasForAttribute(ATTRIBUTE_CLASS));
                        final Class type;
                        if (classAttribute != null) {
                            type = mapper.realClass(classAttribute);
                        } else {
                            type = mapper.defaultImplementationOf(reflectionProvider.getFieldType(result, fieldName, currentType[0]));
                        }
                        Object value = context.convertAnother(result, type);
                        reflectionProvider.writeField(result, fieldName, value, currentType[0]);
                    }
                    reader.moveUp();
                }
                reader.moveUp();
            }

            public void registerValidation(final ObjectInputValidation validation, int priority) {
                context.addCompletionCallback(new Runnable() {

                    public void run() {
                        try {
                            validation.validateObject();
                        } catch (InvalidObjectException e) {
                            throw new ObjectAccessException("Cannot validate object : " + e.getMessage(), e);
                        }
                    }
                }, priority);
            }

            public void close() {
                throw new UnsupportedOperationException("Objects are not allowed to call ObjectInputStream.close() from readObject()");
            }
        };
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            if (nodeName.equals(ELEMENT_UNSERIALIZABLE_PARENTS)) {
                super.doUnmarshal(result, reader, context);
            } else {
                currentType[0] = mapper.defaultImplementationOf(mapper.realClass(nodeName));
                if (serializationMethodInvoker.supportsReadObject(currentType[0], false)) {
                    CustomObjectInputStream objectInputStream = CustomObjectInputStream.getInstance(context, callback);
                    serializationMethodInvoker.callReadObject(currentType[0], result, objectInputStream);
                    objectInputStream.popCallback();
                } else {
                    try {
                        callback.defaultReadObject();
                    } catch (IOException e) {
                        throw new ObjectAccessException("Could not call defaultWriteObject()", e);
                    }
                }
            }
            reader.moveUp();
        }
        return result;
    }
}
