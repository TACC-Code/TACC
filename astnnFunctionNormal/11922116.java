class BackupThread extends Thread {
        protected BeanProperty(String name, Class type, Method read, Method write, Field field) {
            this.name = name;
            this.type = type;
            this.writeMethod = write;
            this.readMethod = read;
            this.field = field;
        }
}
