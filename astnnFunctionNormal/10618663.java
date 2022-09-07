class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public void testAllReadWriteProperties() {
        try {
            for (int i = 0; i < classes.length; i++) {
                Object object = classes[i].newInstance();
                ClassInfo info = ClassInfo.getInstance(classes[i]);
                List writeables = Arrays.asList(info.getWriteablePropertyNames());
                List readables = Arrays.asList(info.getReadablePropertyNames());
                for (int j = 0; j < writeables.size(); j++) {
                    String writeable = (String) writeables.get(j);
                    if (readables.contains(writeable)) {
                        Class type = info.getGetterType(writeable);
                        Object sample = getSampleFor(type);
                        Probe probe = ProbeFactory.getProbe(object);
                        probe.setObject(object, writeable, sample);
                        assertEquals(sample, probe.getObject(object, writeable));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error. ", e);
        }
    }
}
