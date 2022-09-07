class BackupThread extends Thread {
    protected void printThreadLockInfo(ThreadReference thread, PrintWriter writer) throws CommandException {
        try {
            VirtualMachine vm = thread.virtualMachine();
            StringBuilder sb = new StringBuilder(512);
            sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_monitorInfo", thread.name()));
            sb.append('\n');
            if (vm.canGetOwnedMonitorInfo()) {
                List<ObjectReference> owned = thread.ownedMonitors();
                if (owned.size() == 0) {
                    sb.append("  ");
                    sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_noMonitors"));
                    sb.append('\n');
                } else {
                    Iterator<ObjectReference> iter = owned.iterator();
                    while (iter.hasNext()) {
                        ObjectReference monitor = iter.next();
                        sb.append("  ");
                        sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_ownedMonitor", monitor.toString()));
                        sb.append('\n');
                    }
                }
            } else {
                sb.append("  ");
                sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_cannotGetOwnedMonitors"));
                sb.append('\n');
            }
            if (vm.canGetCurrentContendedMonitor()) {
                ObjectReference waiting = thread.currentContendedMonitor();
                sb.append("  ");
                if (waiting == null) {
                    sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_notWaiting"));
                } else {
                    sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_waitingFor", waiting.toString()));
                }
                sb.append('\n');
            } else {
                sb.append("  ");
                sb.append(NbBundle.getMessage(getClass(), "CTL_threadlocks_cannotGetContendedMonitor"));
                sb.append('\n');
            }
            writer.print(sb.toString());
        } catch (UnsupportedOperationException uoe) {
            throw new CommandException(NbBundle.getMessage(getClass(), "CTL_threadlocks_unsupported"), uoe);
        } catch (IncompatibleThreadStateException itse) {
            throw new CommandException(NbBundle.getMessage(getClass(), "ERR_ThreadNotSuspended"), itse);
        }
    }
}
