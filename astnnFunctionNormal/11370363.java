class BackupThread extends Thread {
    private String fillVelocityTemplate(Reader reader) {
        try {
            StringWriter writer = new StringWriter();
            VelocityContext ctx = velocityContextProvider.getVelocityContext();
            boolean isOk = Velocity.evaluate(ctx, writer, VELOCITY_LOG4J_APPENDER_NAME, reader);
            isTrue(isOk);
            writer.close();
            return writer.toString();
        } catch (Exception e) {
            throw createRuntimeException(e);
        }
    }
}
