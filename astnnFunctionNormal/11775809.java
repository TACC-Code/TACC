class BackupThread extends Thread {
    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        org.omg.CORBA.portable.OutputStream _out = null;
        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);
        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
        switch(opsIndex.intValue()) {
            case 0:
                {
                    try {
                        fr.esrf.Tango.AttributeValue[] _arg0 = fr.esrf.Tango.AttributeValueListHelper.read(_input);
                        _out = handler.createReply();
                        write_attributes(_arg0);
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 1:
                {
                    _out = handler.createReply();
                    _out.write_string(name());
                    break;
                }
            case 2:
                {
                    try {
                        _out = handler.createReply();
                        ping();
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 3:
                {
                    _out = handler.createReply();
                    _out.write_string(description());
                    break;
                }
            case 4:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeValueListHelper.write(_out, read_attributes(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 5:
                {
                    _out = handler.createReply();
                    _out.write_string(adm_name());
                    break;
                }
            case 6:
                {
                    try {
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdInfoListHelper.write(_out, command_list_query());
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 7:
                {
                    try {
                        _out = handler.createReply();
                        fr.esrf.Tango.DevInfoHelper.write(_out, info());
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 8:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        org.omg.CORBA.Any _arg1 = _input.read_any();
                        _out = handler.createReply();
                        _out.write_any(command_inout(_arg0, _arg1));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 9:
                {
                    _out = handler.createReply();
                    fr.esrf.Tango.DevStateHelper.write(_out, state());
                    break;
                }
            case 10:
                {
                    _out = handler.createReply();
                    _out.write_string(status());
                    break;
                }
            case 11:
                {
                    try {
                        fr.esrf.Tango.AttributeConfig[] _arg0 = fr.esrf.Tango.AttributeConfigListHelper.read(_input);
                        _out = handler.createReply();
                        set_attribute_config(_arg0);
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 12:
                {
                    try {
                        int _arg0 = _input.read_long();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevVarStringArrayHelper.write(_out, black_box(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 13:
                {
                    try {
                        java.lang.String[] _arg0 = fr.esrf.Tango.DevVarStringArrayHelper.read(_input);
                        _out = handler.createReply();
                        fr.esrf.Tango.AttributeConfigListHelper.write(_out, get_attribute_config(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 14:
                {
                    try {
                        java.lang.String _arg0 = _input.read_string();
                        _out = handler.createReply();
                        fr.esrf.Tango.DevCmdInfoHelper.write(_out, command_query(_arg0));
                    } catch (fr.esrf.Tango.DevFailed _ex0) {
                        _out = handler.createExceptionReply();
                        fr.esrf.Tango.DevFailedHelper.write(_out, _ex0);
                    }
                    break;
                }
        }
        return _out;
    }
}
