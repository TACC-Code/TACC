class BackupThread extends Thread {
    public RecordAccessLibrary(String readerBeanName, String writerBeanName) {
        recordReader = (RecordReader) new ClassPathXmlApplicationContext(RecordReader.class.getName() + ".xml").getBean(readerBeanName);
        recordWriter = (RecordWriter) new ClassPathXmlApplicationContext(RecordWriter.class.getName() + ".xml").getBean(writerBeanName);
    }
}
