class BackupThread extends Thread {
        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.DOUBLE, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putDouble(message, offset, input.readDouble()); else us.putObject(message, offset, new Double(input.readDouble()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeDouble(number, us.getDouble(message, offset), false); else {
                        Double value = (Double) us.getObject(message, offset);
                        if (value != null) output.writeDouble(number, value.doubleValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeDouble(number, input.readDouble(), repeated);
                }
            };
        }
}
