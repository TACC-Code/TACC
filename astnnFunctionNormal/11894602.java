class BackupThread extends Thread {
    public static int tryCopyFields(Object source, Object target, Map<String, Object> readers, Map<String, Object> writers) {
        int res = 0;
        if (source == null || target == null || readers == null || writers == null || readers.size() == 0 || writers.size() == 0) {
            return res;
        }
        for (String name : readers.keySet()) {
            Object reader = readers.get(name);
            Object writer = writers.get(name);
            if (writer == null) {
                continue;
            }
            try {
                Object value = (reader instanceof Method) ? ((Method) reader).invoke(source, (Object[]) null) : ((Field) reader).get(source);
                if (writer instanceof Method) {
                    ((Method) writer).invoke(target, new Object[] { value });
                } else {
                    ((Field) writer).set(target, value);
                }
                res++;
            } catch (Throwable th) {
            }
        }
        return res;
    }
}
