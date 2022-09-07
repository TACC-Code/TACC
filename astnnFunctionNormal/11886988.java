class BackupThread extends Thread {
    public String interfaceHash(String className) throws IllegalArgumentException, NoSuchAlgorithmException {
        MessageDigest md;
        StringBuffer field;
        StringBuffer buf;
        SortedSet sorter;
        Object fieldValue;
        Field[] fields;
        String hash;
        Class clazz;
        String msg;
        Class tmp;
        List data;
        byte[] b;
        int val;
        int i;
        if (className == null) {
            log_.error(msg = "Invalid class name: " + className);
            throw new IllegalArgumentException(msg);
        }
        if ((hash = (String) cache_.get(className)) == null) {
            data = new ArrayList();
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                log_.error(msg = className + " could not be resolved");
                throw new IllegalArgumentException(msg);
            }
            if (!clazz.isInterface()) {
                log_.error(msg = className + " is not an interface");
                throw new IllegalArgumentException(msg);
            }
            data.add(new Integer(clazz.getModifiers()));
            data.add(clazz.getName());
            if ((tmp = clazz.getSuperclass()) != null) {
                data.add(tmp.getName());
            }
            sorter = new TreeSet(new Comparator() {

                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            sorter.addAll(Arrays.asList(clazz.getInterfaces()));
            data.add(sorter.toString());
            sorter.clear();
            fields = clazz.getDeclaredFields();
            for (i = 0; i < fields.length; i++) {
                field = new StringBuffer(fields[i].toString());
                try {
                    fieldValue = fields[i].get(null);
                    if (fieldValue != null) {
                        field.append(" = ");
                        field.append(deepToString(fieldValue));
                    }
                } catch (Exception e) {
                }
                sorter.add(field.toString());
            }
            data.add(sorter.toString());
            sorter.clear();
            sorter.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            data.add(sorter.toString());
            md = MessageDigest.getInstance("SHA1");
            md.update(Charset.forName("UTF-8").encode(data.toString()).array());
            b = md.digest();
            buf = new StringBuffer();
            for (i = 0; i < b.length; i++) {
                if ((val = b[i]) < 0) {
                    val += 0x100;
                }
                if (val < 0x10) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(val));
            }
            cache_.put(className, hash = buf.toString());
            log_.debug("Successfully created hash code for " + className + ": " + hash);
        }
        return hash;
    }
}
