class BackupThread extends Thread {
    public void setDefaults() throws Exception {
        cat.debug("Setting defaults");
        if (conn != null) if (!conn.isClosed()) conn.close();
        setConnection();
        cat.info("Connected to database @ " + Environment.getInstance().getUrl());
        Statement st = conn.createStatement();
        try {
            cat.debug(TABLE_TOPIC);
            st.execute(TABLE_TOPIC);
        } catch (Exception err) {
            cat.debug("Error on create: " + err);
        }
        try {
            cat.debug(TABLE_CHANGE);
            st.execute(TABLE_CHANGE);
        } catch (Exception err) {
            cat.debug("Error on create: " + err);
        }
        try {
            cat.debug(TABLE_VERSION);
            st.execute(TABLE_VERSION);
        } catch (Exception err) {
            cat.debug("Error on create: " + err);
        }
        try {
            cat.debug(TABLE_LOCK);
            st.execute(TABLE_LOCK);
        } catch (Exception err) {
            cat.debug("Error on create: " + err);
        }
        try {
            cat.debug(TABLE_READ_ONLY);
            st.execute(TABLE_READ_ONLY);
        } catch (Exception err) {
            cat.debug("Error on create: " + err);
        }
        st.close();
        readStatement = conn.prepareStatement(STATEMENT_READ);
        insertStatement = conn.prepareStatement(STATEMENT_INSERT);
        updateStatement = conn.prepareStatement(STATEMENT_UPDATE);
        existsStatement = conn.prepareStatement(STATEMENT_EXISTS);
        setLockStatement = conn.prepareStatement(STATEMENT_SET_LOCK);
        removeAnyLockStatement = conn.prepareStatement(STATEMENT_REMOVE_ANY_LOCK);
        removeLockStatement = conn.prepareStatement(STATEMENT_REMOVE_LOCK);
        checkLockStatement = conn.prepareStatement(STATEMENT_CHECK_LOCK);
        getReadOnlyStatement = conn.prepareStatement(STATEMENT_READONLY_ALL);
        deleteReadOnlyStatement = conn.prepareStatement(STATEMENT_READONLY_DELETE);
        addReadOnlyStatement = conn.prepareStatement(STATEMENT_READONLY_INSERT);
        findReadOnlyStatement = conn.prepareStatement(STATEMENT_READONLY_FIND);
        if (!exists("StartingPoints")) {
            cat.debug("Setting up StartingPoints");
            write(WikiBase.readDefaultTopic("StartingPoints"), true, "StartingPoints");
        }
        if (!exists("TextFormattingRules")) {
            cat.debug("Setting up TextFormattingRules");
            write(WikiBase.readDefaultTopic("TextFormattingRules"), true, "TextFormattingRules");
        }
        if (!exists("FritzTextFormattingRules")) {
            cat.debug("Setting up FritzTextFormattingRules");
            write(WikiBase.readDefaultTopic("FritzTextFormattingRules"), true, "FritzTextFormattingRules");
        }
        if (!exists("RecentChanges")) write("", false, "RecentChanges");
        if (!exists("WikiSearch")) write("", false, "WikiSearch");
        if (!exists("SetUsername")) write("", false, "SetUsername");
    }
}
