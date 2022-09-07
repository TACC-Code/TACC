class BackupThread extends Thread {
    @Override
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        VirtualMachine vm = session.getConnection().getVM();
        PrintWriter writer = context.getWriter();
        String token = arguments.nextToken();
        ThreadReference thread = Threads.findThread(vm, token);
        if (thread == null) {
            throw new CommandException(NbBundle.getMessage(InterruptCommand.class, "ERR_ThreadNotFound", token));
        } else {
            thread.interrupt();
            writer.println(NbBundle.getMessage(InterruptCommand.class, "CTL_interrupt_Interrupted", thread.uniqueID()));
        }
    }
}
