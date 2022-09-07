class BackupThread extends Thread {
    @Override
    public SWTGuiWidget build(Attribute attrib, JfgFormData data, InnerBuilder innerBuilder) {
        if (!innerBuilder.canBuildInnerAttribute()) return null;
        if (attrib.canWrite()) System.out.println("[JFG] Creating GUI for read/write object. " + "I'll only change the object in place and will not check for changes in it!");
        return new InlineObjectListSWTWidget(attrib, data);
    }
}
