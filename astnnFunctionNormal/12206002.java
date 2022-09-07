class BackupThread extends Thread {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.FIXED64, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    us.putObject(message, offset, new Date(input.readFixed64()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    Date value = (Date) us.getObject(message, offset);
                    if (value != null) output.writeFixed64(number, value.getTime(), false);
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeFixed64(number, input.readFixed64(), repeated);
                }
            };
        }
}
