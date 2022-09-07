class BackupThread extends Thread {
    private boolean readDeviceInfo() throws Exception {
        DeviceIdentification di = this.output_writer.getDeviceIdentification();
        String read_sw_ver_create = getCommand(OneTouchMeter2.COMMAND_READ_SW_VERSION_AND_CREATE);
        this.output_writer.setSubStatus(ic.getMessage("READING_SW_VERSION"));
        write(hex_utils.reconvert(read_sw_ver_create));
        String sw_dd = tryToConvert(this.readLineBytes(), 6 + 6, 3, false);
        int idx = sw_dd.indexOf("/") - 2;
        if (idx == -3) {
            return false;
        }
        this.output_writer.setSpecialProgress(2);
        cmdAcknowledge();
        di.device_hardware_version = sw_dd.substring(0, idx) + ", " + sw_dd.substring(idx);
        this.output_writer.setSubStatus(ic.getMessage("READING_SERIAL_NR"));
        String read_serial_nr = getCommand(OneTouchMeter2.COMMAND_READ_SERIAL_NUMBER);
        write(hex_utils.reconvert(read_serial_nr));
        String sn = tryToConvert(this.readLineBytes(), 6 + 5, 3, false);
        di.device_serial_number = sn;
        this.cmdAcknowledge();
        this.output_writer.setSpecialProgress(4);
        this.output_writer.writeDeviceIdentification();
        return true;
    }
}
