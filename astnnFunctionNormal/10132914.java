class BackupThread extends Thread {
    public void perform(CommandContext context, CommandArguments arguments) throws CommandException, MissingArgumentsException {
        Session session = context.getSession();
        PrintWriter writer = context.getWriter();
        VirtualMachine vm = session.getConnection().getVM();
        DebuggingContext dc = context.getDebuggingContext();
        ThreadReference current = dc.getThread();
        if (!arguments.hasMoreTokens()) {
            if (current == null) {
                throw new CommandException(NbBundle.getMessage(getClass(), "ERR_NoThread"));
            } else {
                printThreadLockInfo(current, writer);
            }
        } else {
            String token = arguments.nextToken();
            if (token.equals("all")) {
                List<ThreadReference> threads = vm.allThreads();
                for (ThreadReference thread : threads) {
                    printThreadLockInfo(thread, writer);
                }
            } else {
                ThreadReference thread = Threads.findThread(vm, token);
                if (thread != null) {
                    printThreadLockInfo(thread, writer);
                } else {
                    throw new CommandException(NbBundle.getMessage(getClass(), "ERR_InvalidThreadID"));
                }
            }
        }
    }
}
