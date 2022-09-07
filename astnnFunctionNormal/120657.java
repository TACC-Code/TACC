class BackupThread extends Thread {
    public boolean transform(XSLProcessor xslprocessor, OutputMethodHandlerImpl outputmethodhandlerimpl, Reader reader, Reader reader1, Writer writer) {
        Object obj;
        if (writer == null) {
            obj = new FileDescriptorDestination(FileDescriptor.out);
        } else {
            obj = new WriterDestination(writer);
        }
        outputmethodhandlerimpl.setDestination(((Destination) (obj)));
        return transform(xslprocessor, new InputSource(reader1), new InputSource(reader));
    }
}
