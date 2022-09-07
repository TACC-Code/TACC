class BackupThread extends Thread {
        public <T> Field<T> create(int number, java.lang.String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.BOOL, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putBoolean(message, offset, input.readBool()); else us.putObject(message, offset, input.readBool() ? Boolean.TRUE : Boolean.FALSE);
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeBool(number, us.getBoolean(message, offset), false); else {
                        Boolean value = (Boolean) us.getObject(message, offset);
                        if (value != null) output.writeBool(number, value.booleanValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeBool(number, input.readBool(), repeated);
                }
            };
        }
}
