class BackupThread extends Thread {
    @Override
    public final boolean update() throws RecordException {
        if (frozen) {
            throw new RecordException("The object is frozen.");
        }
        boolean toReturn = false;
        Class<? extends Record> actualClass = this.getClass();
        HashMap<String, Integer> columns = getColumns(TableNameResolver.getTableName(actualClass));
        LoggableStatement pStat = null;
        Connection conn = ConnectionManager.getConnection();
        try {
            if (exists()) {
                StatementBuilder builder = new StatementBuilder("update " + TableNameResolver.getTableName(actualClass) + " set");
                String updates = "";
                for (String key : columns.keySet()) {
                    if (!key.equals("id")) {
                        updates += key + " = :" + key + ", ";
                        Field f = FieldHandler.findField(actualClass, key);
                        builder.set(key, FieldHandler.getValue(f, this));
                    }
                }
                builder.append(updates.substring(0, updates.length() - 2));
                builder.append("where id = :id");
                builder.set(":id", FieldHandler.getValue(FieldHandler.findField(actualClass, "id"), this));
                pStat = builder.getPreparedStatement(conn);
                log.log(pStat.getQueryString());
                int i = pStat.executeUpdate();
                toReturn = i == 1;
            } else {
                throw new RecordException(Messages.SR_UPDATE_IMPOSSIBLE);
            }
            if (childList != null) {
                if (childObjects == null) {
                    childObjects = new HashMap<Class<? extends Record>, Record>();
                }
                for (Class<? extends Record> c : childList.keySet()) {
                    childObjects.get(c).update();
                }
            }
            if (childrenList != null) {
                if (childrenObjects == null) {
                    childrenObjects = new HashMap<Class<? extends Record>, List<? extends Record>>();
                }
                for (Class<? extends Record> c : childrenList.keySet()) {
                    for (Record r : childrenObjects.get(c)) {
                        r.update();
                    }
                }
            }
            if (relatedList != null) {
                if (childrenObjects == null) {
                    childrenObjects = new HashMap<Class<? extends Record>, List<? extends Record>>();
                }
                for (Class<? extends Record> c : relatedList.keySet()) {
                    for (Record r : childrenObjects.get(c)) {
                        r.update();
                    }
                }
            }
            return toReturn;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException(e);
        } finally {
            try {
                if (pStat != null) {
                    pStat.close();
                }
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                throw new RecordException("Error closing connection");
            }
        }
    }
}
