class BackupThread extends Thread {
    public void receiveShare(SFSClientListener listener, SFSClient sfsClient) {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            String toSend = hash + " " + totalParts + " " + partNumber;
            out.write((toSend + '\n').getBytes());
            ShareFileWriter writer = new ShareFileWriter(part, new File(sfsClient.getShareFolder() + part.getName()));
            LocalShare ls = sfsClient.getLocalShares().get(hash);
            byte[] buf = new byte[socket.getReceiveBufferSize()];
            int read;
            long tot = 0;
            while (tot < part.getSize()) {
                read = in.read(buf);
                writer.write(buf, read);
                tot += read;
                listener.receiveStatus(ls, part, partNumber, read);
            }
            ls.incShares();
            if (ls.getShares() == ls.getTotalShares()) {
                listener.receiveDone(ls);
                sfsClient.getClient().sendObject(new DownloadCompleteEvent(hash));
            }
        } catch (IOException ex) {
            Logger.getLogger(TransferShareEvent.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(TransferShareEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
