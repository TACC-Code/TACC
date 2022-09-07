class BackupThread extends Thread {
        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.FLOAT, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putFloat(message, offset, input.readFloat()); else us.putObject(message, offset, new Float(input.readFloat()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeFloat(number, us.getFloat(message, offset), false); else {
                        Float value = (Float) us.getObject(message, offset);
                        if (value != null) output.writeFloat(number, value.floatValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeFloat(number, input.readFloat(), repeated);
                }
            };
        }
}
