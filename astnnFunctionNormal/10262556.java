class BackupThread extends Thread {
    public ConfigurationItem(TrancheServer ts, TrancheServerCommandLineClient clc) {
        super(ts, clc, "configuration", "Manipulates the server's configuration parameters.");
        addAttribute("command", "The command to run. Type 'configuration help' to see a list of the possible commands.", true);
        addAttribute("name", "The name given to the server.");
        addAttribute("group", "The name of the group to which the server belongs.");
        addAttribute("mode", "Valid values include: none, read, write, read-write.");
        addAttribute("hashspans", "The number of hash spans to be entered.");
        this.ts = (FlatFileTrancheServer) ts;
    }
}
