class BackupThread extends Thread {
    protected ParameterArray composeInputArray() {
        VSMString tempString = null;
        ParameterArray parameterArray = new ParameterArray(this);
        tempString = new VSMString(getFunctionName(), getFunctionName());
        parameterArray.add(new VSMInt4(tempString.paramLength(), "function_name_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(getUserid(), "authenticated_userid");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "authenticated_userid_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(getPassword(), "password");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "password_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(getTarget_identifier(), "target_identifier");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "target_identifier_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(get_imageDiskNumber(), "image_disk_number");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "image_disk_number_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(get_targetImageName(), "target_image_name");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "target_image_name_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(get_targetImageDiskNumber(), "target_image_disk_number");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "target_image_disk_number_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(get_readWriteMode(), "read_write_mode");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "read_write_mode_length"));
        parameterArray.add(tempString);
        tempString = new VSMString(get_optionalPassword(), "optional_password");
        parameterArray.add(new VSMInt4(tempString.paramLength(), "optional_password_length"));
        parameterArray.add(tempString);
        VSMInt4 outputLength = new VSMInt4(new Long(parameterArray.totalParameterLength()).intValue(), "output_length");
        parameterArray.insertElementAt(outputLength, 0);
        setInParams(parameterArray);
        return parameterArray;
    }
}
