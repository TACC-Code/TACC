class BackupThread extends Thread {
    private void printHelp() throws IOException {
        writeLine("bucket [bucketname]");
        writeLine("copy <id> <src_bucket> <dest_bucket> [user] [password]");
        writeLine("copyall [prefix] <src_bucket> <dest_bucket> [user] [password]");
        writeLine("count [prefix]");
        writeLine("createbucket");
        writeLine("delete <id>");
        writeLine("deleteall [prefix]");
        writeLine("deletebucket");
        writeLine("exit");
        writeLine("get <id>");
        writeLine("getacl ['bucket'|'item'] <id>");
        writeLine("getfile <id> <file>");
        writeLine("getfilez <id> <file>");
        writeLine("gettorrent <id>");
        writeLine("head ['bucket'|'item'] <id>");
        writeLine("host [hostname]");
        writeLine("list [prefix] [max]");
        writeLine("listatom [prefix] [max]");
        writeLine("listrss [prefix] [max]");
        writeLine("listbuckets");
        writeLine("pass [password]");
        writeLine("put <id> <data>");
        writeLine("putfile <id> <file>");
        writeLine("putfilecontenttype <id> <file> <content-type>");
        writeLine("putfilez <id> <file>");
        writeLine("putfilewacl <id> <file> ['private'|'public-read'|'public-read-write'|'authenticated-read']");
        writeLine("putfilezwacl <id> <file> ['private'|'public-read'|'public-read-write'|'authenticated-read']");
        writeLine("quit");
        writeLine("setacl ['bucket'|'item'] <id> ['private'|'public-read'|'public-read-write'|'authenticated-read']");
        writeLine("time ['none'|'long'|'all']");
        writeLine("threads [num]");
        writeLine("user [username]");
    }
}
