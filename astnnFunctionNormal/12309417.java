class BackupThread extends Thread {
    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        ContextManager ctxtMgr = (ContextManager) session.getManager(ContextManager.class);
        ThreadReference current = ctxtMgr.getCurrentThread();
        if (!args.hasMoreTokens()) {
            if (current == null) {
                throw new CommandException(Bundle.getString("noCurrentThread"));
            } else {
                printStack(current, out, ctxtMgr);
            }
        } else {
            String token = args.nextToken();
            if (token.toLowerCase().equals("all")) {
                Iterator iter = session.getVM().allThreads().iterator();
                while (iter.hasNext()) {
                    ThreadReference thread = (ThreadReference) iter.next();
                    out.writeln(thread.name() + ": ");
                    try {
                        printStack(thread, out, ctxtMgr);
                    } catch (CommandException ce) {
                        out.writeln(ce.getMessage());
                    }
                }
            } else {
                VirtualMachine vm = session.getConnection().getVM();
                ThreadReference thread = Threads.getThreadByID(vm, token);
                if (thread != null) {
                    printStack(thread, out, ctxtMgr);
                } else {
                    throw new CommandException(Bundle.getString("invalidThreadID"));
                }
            }
        }
    }
}
