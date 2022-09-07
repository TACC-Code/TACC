class BackupThread extends Thread {
    public RequestEntity(Entity entity) {
        this.builder = new FormBuilder(this, entity);
        this.profile = new Profile(this);
        this.channel = entity.getChannel();
        this.header = entity.getHeader();
        this.entity = entity;
    }
}
