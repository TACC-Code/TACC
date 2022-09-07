class BackupThread extends Thread {
    protected void preReadBlockContent() throws EXIException {
        try {
            if (blockValues <= Constants.MAX_NUMBER_OF_VALUES) {
                List<QNameContext> order = decoderContext.getChannelOrders();
                for (QNameContext o : order) {
                    RuntimeQNameContextEntries rqnce = decoderContext.getRuntimeQNameContextEntries(o);
                    List<ValueAndDatatype> lvd = rqnce.getValuesAndDataypes();
                    Value[] contentValues = new Value[lvd.size()];
                    for (int i = 0; i < lvd.size(); i++) {
                        ValueAndDatatype vd = lvd.get(i);
                        contentValues[i] = typeDecoder.readValue(vd.datatype, decoderContext, o, channel);
                    }
                    PreReadValue prv = new PreReadValue(contentValues);
                    rqnce.setPreReadValue(prv);
                }
            } else {
                DecoderChannel bdcLessEqual100 = null;
                List<QNameContext> orderLeq100 = decoderContext.getChannelOrders();
                for (QNameContext o : orderLeq100) {
                    RuntimeQNameContextEntries rqnce = decoderContext.getRuntimeQNameContextEntries(o);
                    List<ValueAndDatatype> lvd = rqnce.getValuesAndDataypes();
                    if (lvd.size() <= Constants.MAX_NUMBER_OF_VALUES) {
                        Value[] contentValues = new Value[lvd.size()];
                        if (bdcLessEqual100 == null) {
                            bdcLessEqual100 = getNextChannel();
                        }
                        for (int i = 0; i < lvd.size(); i++) {
                            ValueAndDatatype vd = lvd.get(i);
                            contentValues[i] = typeDecoder.readValue(vd.datatype, decoderContext, o, bdcLessEqual100);
                        }
                        PreReadValue prv = new PreReadValue(contentValues);
                        rqnce.setPreReadValue(prv);
                    }
                }
                List<QNameContext> orderGr100 = decoderContext.getChannelOrders();
                for (QNameContext o : orderGr100) {
                    RuntimeQNameContextEntries rqnce = decoderContext.getRuntimeQNameContextEntries(o);
                    List<ValueAndDatatype> lvd = rqnce.getValuesAndDataypes();
                    if (lvd.size() > Constants.MAX_NUMBER_OF_VALUES) {
                        DecoderChannel bdcGreater100 = getNextChannel();
                        Value[] contentValues = new Value[lvd.size()];
                        for (int i = 0; i < lvd.size(); i++) {
                            ValueAndDatatype vd = lvd.get(i);
                            contentValues[i] = typeDecoder.readValue(vd.datatype, decoderContext, o, bdcGreater100);
                        }
                        PreReadValue prv = new PreReadValue(contentValues);
                        rqnce.setPreReadValue(prv);
                    }
                }
            }
        } catch (IOException e) {
            throw new EXIException(e);
        }
    }
}
