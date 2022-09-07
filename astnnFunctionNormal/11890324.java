class BackupThread extends Thread {
    @Override
    public boolean visit(CompilationUnit unit) {
        ITypeBinding ser = getSerializable();
        NodeLookup nodes = getNodeLookup(unit);
        for (TypeDeclaration node : nodes.getNodes(TypeDeclaration.class)) {
            ITypeBinding type = node.resolveBinding();
            if ((null == type) || !type.isAssignmentCompatible(ser)) {
                continue;
            }
            MethodDeclaration readObject = null;
            MethodDeclaration writeObject = null;
            for (MethodDeclaration method : node.getMethods()) {
                IMethodBinding binding = method.resolveBinding();
                if (!isSerializationMethod(binding, false)) {
                    continue;
                }
                if ("readObject".equals(binding.getName())) {
                    readObject = method;
                } else if ("writeObject".equals(binding.getName())) {
                    writeObject = method;
                }
            }
            if ((null != readObject) && (null == writeObject)) {
                addProblem(readObject.getName(), "readObject", "writeObject");
            } else if ((null != writeObject) && (null == readObject)) {
                addProblem(writeObject.getName(), "writeObject", "readObject");
            }
        }
        return false;
    }
}
