class BackupThread extends Thread {
    @Test
    public void test11() throws Exception {
        IndexWriter writer = new IndexWriter("D:\\workg\\index", new StandardAnalyzer(), false);
        Field field = writer.getClass().getDeclaredField("segmentInfos");
        field.setAccessible(true);
        Object infos = field.get(writer);
        Method[] methods = writer.getClass().getDeclaredMethods();
        Method target = null;
        for (Method method : methods) {
            if ("mergeSegments".equals(method.getName())) {
                target = method;
                break;
            }
        }
        target.setAccessible(true);
        target.invoke(writer, infos, Integer.parseInt(Test4.reader.readLine()), Integer.parseInt(Test4.reader.readLine()));
        writer.close();
    }
}
