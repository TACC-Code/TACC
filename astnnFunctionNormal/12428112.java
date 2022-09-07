class BackupThread extends Thread {
    public void write(ReaderVo readerVo) {
        String readerId = readerVo.getReaderId();
        String readerName = readerVo.getReaderName();
        int count = 0;
        try {
            count = this.jdbcTemplate.queryForInt(this.sqlStatements.get(existSql), readerId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.initTable();
        }
        if (count > 0) {
            this.jdbcTemplate.update(this.sqlStatements.get(updateSql), readerId, readerName, readerId);
        } else {
            this.jdbcTemplate.update(this.sqlStatements.get(insertSql), readerId, readerName);
        }
    }
}
