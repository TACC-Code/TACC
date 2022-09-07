class BackupThread extends Thread {
    private org.omg.CORBA.portable.OutputStream _OB_op_connect_typed_push_consumer(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        try {
            org.omg.CosTypedEventComm.TypedPushConsumer _ob_a0 = org.omg.CosTypedEventComm.TypedPushConsumerHelper.read(in);
            connect_typed_push_consumer(_ob_a0);
            out = handler.createReply();
        } catch (org.omg.CosEventChannelAdmin.AlreadyConnected _ob_ex) {
            out = handler.createExceptionReply();
            org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.write(out, _ob_ex);
        } catch (org.omg.CosEventChannelAdmin.TypeError _ob_ex) {
            out = handler.createExceptionReply();
            org.omg.CosEventChannelAdmin.TypeErrorHelper.write(out, _ob_ex);
        }
        return out;
    }
}
