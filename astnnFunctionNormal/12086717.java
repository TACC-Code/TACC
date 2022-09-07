class BackupThread extends Thread {
    private void writePartList(Node parent) {
        Node partList = this.addNode(parent, "part-list");
        GMChannelRouter gmChannelRouter = new GMChannelRouter();
        GMChannelRouterConfigurator gmChannelRouterConfigurator = new GMChannelRouterConfigurator(gmChannelRouter);
        gmChannelRouterConfigurator.configureRouter(this.manager.getSong());
        Iterator tracks = this.manager.getSong().getTracks();
        while (tracks.hasNext()) {
            TGTrack track = (TGTrack) tracks.next();
            TGChannel channel = this.manager.getChannel(track.getChannelId());
            Node scoreParts = this.addNode(partList, "score-part");
            this.addAttribute(scoreParts, "id", "P" + track.getNumber());
            this.addNode(scoreParts, "part-name", track.getName());
            if (channel != null) {
                GMChannelRoute gmChannelRoute = gmChannelRouter.getRoute(channel.getChannelId());
                Node scoreInstrument = this.addAttribute(this.addNode(scoreParts, "score-instrument"), "id", "P" + track.getNumber() + "-I1");
                this.addNode(scoreInstrument, "instrument-name", channel.getName());
                Node midiInstrument = this.addAttribute(this.addNode(scoreParts, "midi-instrument"), "id", "P" + track.getNumber() + "-I1");
                this.addNode(midiInstrument, "midi-channel", Integer.toString(gmChannelRoute != null ? gmChannelRoute.getChannel1() + 1 : 16));
                this.addNode(midiInstrument, "midi-program", Integer.toString(channel.getProgram() + 1));
            }
        }
    }
}
