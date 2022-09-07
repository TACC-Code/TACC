class BackupThread extends Thread {
    public Object readScriptObject(String className) throws Exception {
        Object object;
        if (className.equalsIgnoreCase("undefined") || className == null || className.length() == 0) {
            object = new ASObject();
        } else {
            object = new ASObject();
            ((ASObject) object).setType(className);
        }
        int objectId = this.rememberObject(object);
        if (isDebug) trace.writeString("amf0Input.readScriptObject.startAMFObject" + className);
        String propertyName = in.readUTF();
        byte type = in.readByte();
        while (type != kObjectEndType) {
            if (isDebug) trace.writeString("amf0Input.readScriptObject.namedElement" + propertyName);
            Object value = this.readObjectValue(type);
            ((ASObject) object).put(propertyName, value);
            propertyName = in.readUTF();
            type = in.readByte();
        }
        if (isDebug) trace.writeString("amf0Input.readScriptObject.endAMFObject");
        return object;
    }
}
