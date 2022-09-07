class BackupThread extends Thread {
    public Board getBoardForCommChannelNameAndAddress(String channelName, int address) {
        Board foundBoard = null;
        for (Board currBoard : this.allBoardsFound) {
            if (currBoard.getCommChannel().getChannelName().equals(channelName) && currBoard.getAddress() == address) {
                foundBoard = currBoard;
                break;
            }
        }
        return foundBoard;
    }
}
