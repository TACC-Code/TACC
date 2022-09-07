class BackupThread extends Thread {
    void handleSocketOp(Object obj, STUNTSelector sel) throws Exception {
        STUNTSocket so = null;
        STUNTServerSocket ss = null;
        if (obj instanceof STUNTSocket) so = (STUNTSocket) obj;
        if (obj instanceof STUNTServerSocket) ss = (STUNTServerSocket) obj;
        if (so != null && so == bsock) {
            if (bsock.isConnected()) {
                fsock = bsock;
                bsock = null;
                closeSocket(ASOCK);
                verifyConnected(fsock);
                setState(isActive ? State.A_ECHO : State.P_ECHO);
                log.finest("Direct connect succeeded. Verifying.");
                return;
            }
        }
        if (ss != null && ss == psock) {
            fsock = psock.accept();
            if (fsock != null) {
                closeSocket(PSOCK);
                verifyConnected(fsock);
                setState(isActive ? State.A_ECHO : State.P_ECHO);
                log.finest("Accepted direct connect, verifying.");
            }
        }
        switch(mState) {
            case P_ST1:
            case P_ST2:
            case A_INV_ACKD:
            case P_CB_RCVD:
                if (so != null && so == bsock) {
                    if (!bsock.isConnected()) {
                        closeSocket(BSOCK);
                        log.finest("Direct connect failed.");
                        if (mState == State.P_CB_RCVD) abort(new SocketTimeoutException("Connect Timeout"));
                    }
                    return;
                }
        }
        switch(mState) {
            case P_ST1:
            case A_ST1:
                if (so != null && so == asock) {
                    if (asock.isConnected()) {
                        iaddr = asock.s.getLocalSocketAddress();
                        eaddrs[0] = (InetSocketAddress) readOne(asock);
                        asock = getNBConnSock(iaddr, responders[1], sel, false);
                        setState(isActive ? State.A_ST2 : State.P_ST2);
                        log.finest("First STUNT lookup successful(" + iaddr + "->" + eaddrs[0] + "), attemting second STUNT lookup.");
                    } else {
                        abort(new UnknownHostException("Failed contacting STUNT responder #1"));
                    }
                }
                break;
            case P_ST2:
            case A_ST2:
                if (so != null && so == asock) {
                    if (asock.isConnected()) {
                        eaddrs[1] = (InetSocketAddress) readOne(asock);
                        asock = null;
                        psock = getNBLstnSock(iaddr, sel);
                        log.finest("Second STUNT lookup successful(" + iaddr + "->" + eaddrs[1] + ").");
                        if (isActive) {
                            setState(State.A_INV_SENT);
                            send(stuntState.invite(predict()));
                            log.finest("Active client inviting passive, listening for direct connect.");
                        } else {
                            setState(State.P_INV_ACKD);
                            send(stuntState.accept(predict()));
                            log.finest("Passive client accepting inviting, listening for direct connect.");
                        }
                    } else {
                        abort(new UnknownHostException("Failed contacting STUNT responder #2"));
                    }
                }
                break;
            case P_ECHO:
            case A_ECHO:
                if (so != null && so == fsock) {
                    writeInt(fsock, readInt(fsock) ^ STUNT_MAGIC ^ id.hashCode());
                    setState(isActive ? State.A_DONE : State.P_DONE);
                    log.finest("Echoed peer's half-pipe check, checking half-pipe.");
                }
                break;
            case P_DONE:
            case A_DONE:
                if (so != null && so == fsock) {
                    if ((readInt(fsock) ^ STUNT_MAGIC ^ dst.hashCode()) == nonce) {
                        setState(State.STOP);
                        established = true;
                        fsock.stopSelect();
                        log.finest("Connection is full-pipe and verified. Success!!!");
                    } else {
                        abort(new ProtocolException("Connection verification failed."));
                    }
                }
                break;
            default:
                log.fine("Unhandled STUNTSocket event: " + obj.toString());
                if (so != null) {
                    so.close();
                    if (so == fsock) fsock = null; else if (so == asock) asock = null; else if (so == bsock) bsock = null;
                }
                if (ss != null) {
                    ss.close();
                    if (ss == psock) psock = null;
                }
                break;
        }
    }
}
