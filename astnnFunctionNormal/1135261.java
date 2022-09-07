class BackupThread extends Thread {
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.connectTimeout);
        out.writeObject(this.readTimeout);
        out.writeObject(this.useCaches);
    }
}
