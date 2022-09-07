class BackupThread extends Thread {
    public ImageDiskShare(String hostname, int port, String userid, String password, String target_identifier, String image_disk_number, String target_image_name, String target_image_disk_number, String read_write_mode, String optional_password) {
        this();
        setHostname(hostname);
        setPort(port);
        setUserid(userid);
        setPassword(password);
        setTarget_identifier(target_identifier);
        set_imageDiskNumber(image_disk_number);
        set_targetImageName(target_image_name);
        set_targetImageDiskNumber(target_image_disk_number);
        set_readWriteMode(read_write_mode);
        set_optionalPassword(optional_password);
    }
}
