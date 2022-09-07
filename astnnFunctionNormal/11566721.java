class BackupThread extends Thread {
    public static void main(String[] argv) throws IOException, VSMException {
        ImageDiskShare instance = null;
        if (argv.length != 10) {
            System.out.println("usage: args are:\ninetaddr port user pw target image_disk_number target_image_name target_image_disk_number read_write_mode optional_password");
            System.exit(1);
        }
        System.out.println("Args are: " + argv[0] + " " + argv[1] + " " + argv[2] + " " + argv[3] + " " + argv[4] + " " + argv[5] + " " + argv[6] + " " + argv[7] + " " + argv[8] + " " + argv[9]);
        instance = new ImageDiskShare(argv[0], Integer.valueOf(argv[1]).intValue(), argv[2], argv[3], argv[4], argv[5], argv[6], argv[7], argv[8], argv[9]);
        ParameterArray pA = instance.doIt();
        System.out.println("Returns from call to " + instance.getFunctionName() + ":");
        System.out.println(pA.prettyPrintAll());
    }
}
