class BackupThread extends Thread {
    private void outputInterfaceMethods(GinjectorBindings bindings, TypeLiteral<?> ginjectorInterface, SourceWriteUtil sourceWriteUtil, SourceWriter writer) throws NoSourceNameException, UnableToCompleteException {
        NameGenerator nameGenerator = bindings.getNameGenerator();
        for (MethodLiteral<?, Method> method : constructorInjectCollector.getMethods(ginjectorInterface)) {
            Key<?> methodKey = guiceUtil.getKey(method);
            Binding binding = bindings.getBinding(methodKey);
            if (binding == null) {
                logger.log(TreeLogger.Type.ERROR, "Unable to find a binding for the required key " + methodKey);
                throw new UnableToCompleteException();
            }
            if (!reachabilityAnalyzer.isReachable(binding)) {
                PrettyPrinter.log(logger, TreeLogger.Type.ERROR, "The key %s is required by the Ginjector, but is not reachable.", methodKey);
                throw new UnableToCompleteException();
            }
            FragmentPackageName fragmentPackageName = fragmentPackageNameFactory.create(binding.getGetterMethodPackage());
            String body = String.format("return %s.%s().%s();", ginjectorNameGenerator.getFieldName(bindings), nameGenerator.getFragmentGetterMethodName(fragmentPackageName), nameGenerator.getGetterMethodName(guiceUtil.getKey(method)));
            String readableDeclaration = ReflectUtil.signatureBuilder(method).removeAbstractModifier().build();
            sourceWriteUtil.writeMethod(writer, readableDeclaration, body.toString());
        }
        for (MethodLiteral<?, Method> method : memberInjectCollector.getMethods(ginjectorInterface)) {
            Key<?> injectee = guiceUtil.getKey(method);
            if (!reachabilityAnalyzer.isReachableMemberInject(bindings, injectee.getTypeLiteral())) {
                PrettyPrinter.log(logger, TreeLogger.Type.ERROR, "Method injection of %s is required by the Ginjector, but is not reachable.", injectee.getTypeLiteral());
                throw new UnableToCompleteException();
            }
            FragmentPackageName fragmentPackageName = fragmentPackageNameFactory.create(ReflectUtil.getUserPackageName(injectee.getTypeLiteral()));
            String body = String.format("%s.%s().%s(param);", ginjectorNameGenerator.getFieldName(bindings), nameGenerator.getFragmentGetterMethodName(fragmentPackageName), nameGenerator.getMemberInjectMethodName(injectee.getTypeLiteral()));
            String readableDeclaration = ReflectUtil.signatureBuilder(method).withParameterNames(new String[] { "param" }).removeAbstractModifier().build();
            sourceWriteUtil.writeMethod(writer, readableDeclaration, body);
        }
    }
}
