class BackupThread extends Thread {
        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.INT64, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putLong(message, offset, input.readInt64()); else us.putObject(message, offset, Long.valueOf(input.readInt64()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeInt64(number, us.getLong(message, offset), false); else {
                        Long value = (Long) us.getObject(message, offset);
                        if (value != null) output.writeInt64(number, value.longValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeInt64(number, input.readInt64(), repeated);
                }
            };
        }
}
