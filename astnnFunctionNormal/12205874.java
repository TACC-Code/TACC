class BackupThread extends Thread {
        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.INT32, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putInt(message, offset, input.readInt32()); else us.putObject(message, offset, Integer.valueOf(input.readInt32()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeInt32(number, us.getInt(message, offset), false); else {
                        Integer value = (Integer) us.getObject(message, offset);
                        if (value != null) output.writeInt32(number, value.intValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeInt32(number, input.readInt32(), repeated);
                }
            };
        }
}
