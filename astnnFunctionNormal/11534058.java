class BackupThread extends Thread {
    @Override
    public void transfer(Entity2 f) {
        Feature2 f2 = (Feature2) f;
        super.transfer(f2);
        f2.setModel(this.getModel().getId());
        for (Relationship r : this.getRels()) {
            f2.addRel(r.getId());
        }
        for (Comment c : this.getComments()) {
            Comment2 c2 = new Comment2();
            c.transfer(c2);
            f2.addComment(c2);
        }
        for (Map.Entry<String, Attribute> e : this.getAttrs().entrySet()) {
            f2.addAttr(EntityUtil.transferFromAttr(e.getValue()));
        }
    }
}
