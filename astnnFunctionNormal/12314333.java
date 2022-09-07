class BackupThread extends Thread {
    public void testFilterCopyOnlyRelationsTo() {
        FilterManager.instance().clearFilter();
        Session session = sf.getCurrentSession();
        session.beginTransaction();
        SQLQuery q = sf.getCurrentSession().createSQLQuery("CREATE ROLE test NOINHERIT VALID UNTIL 'infinity';");
        q.executeUpdate();
        q = session.createSQLQuery("CREATE ROLE \"loginTest\" LOGIN PASSWORD 'xxx' VALID UNTIL 'infinity';");
        q.executeUpdate();
        session.createSQLQuery("GRANT \"test\" TO \"loginTest\";").executeUpdate();
        session.createSQLQuery("GRANT ALL ON TABLE person TO \"test\";").executeUpdate();
        session.createSQLQuery("GRANT ALL ON TABLE book TO \"test\";").executeUpdate();
        session.createSQLQuery("GRANT ALL ON TABLE r_author_book TO \"test\";").executeUpdate();
        session.createSQLQuery("GRANT ALL ON TABLE comment TO \"test\";").executeUpdate();
        session.createSQLQuery("GRANT ALL ON TABLE r_comment_author_book TO \"test\";").executeUpdate();
        session.createSQLQuery("GRANT SELECT ON TABLE author TO \"test\";").executeUpdate();
        session.getTransaction().commit();
        DTOSession dtoSession = DTOSessionFactory.getSession(DTOSessionFactory.createSession("test", dtoAccessService));
        AuthorDTO aAuthorDto = (AuthorDTO) dtoService.doLoad(aid, AuthorDTO.class, dtoSession);
        assertTrue(aAuthorDto.getBooksReadOnly().size() > 0);
        assertTrue(aAuthorDto.getCommentBooksReadOnly().size() > 0);
        assertNotNull(aAuthorDto.getPerson());
        FilterManager.instance().clearFilter();
        FilterManager.instance().registerBLoMoFilter(AuthorDTO.class, BookDTO.class, "books", "#equal(#methodResult(#filterPar(entity),getTitle),bogus)", 1, null);
        dtoSession = DTOSessionFactory.getSession(DTOSessionFactory.createSession("test", dtoAccessService));
        aAuthorDto = (AuthorDTO) dtoService.doLoad(aid, AuthorDTO.class, dtoSession);
        assertTrue(aAuthorDto.getBooksReadOnly().size() == 0);
        assertTrue(aAuthorDto.getCommentBooksReadOnly().size() > 0);
        assertNotNull(aAuthorDto.getPerson());
        dtoService.doSave(aAuthorDto, dtoSession);
        session = sf.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("REVOKE ALL ON TABLE author FROM \"test\";").executeUpdate();
        session.createSQLQuery("REVOKE ALL ON TABLE person FROM \"test\";").executeUpdate();
        session.createSQLQuery("REVOKE ALL ON TABLE book FROM \"test\";").executeUpdate();
        session.createSQLQuery("REVOKE ALL ON TABLE r_author_book FROM \"test\";").executeUpdate();
        session.createSQLQuery("REVOKE ALL ON TABLE comment FROM \"test\";").executeUpdate();
        session.createSQLQuery("REVOKE ALL ON TABLE r_comment_author_book FROM \"test\";").executeUpdate();
        session.createSQLQuery("DROP ROLE \"loginTest\"").executeUpdate();
        session.createSQLQuery("DROP ROLE \"test\"").executeUpdate();
        session.getTransaction().commit();
        session = sf.getCurrentSession();
        Transaction t = session.beginTransaction();
        t.begin();
        String fn = null, ln = null, title = null, text = null;
        boolean hasPerson = false, hasBook = false, hasComment = false;
        try {
            Author a = (Author) session.load(Author.class, aid);
            Person p = (Person) session.load(Person.class, pid);
            Book b = (Book) session.load(Book.class, bid);
            Comment c = (Comment) session.load(Comment.class, cid);
            fn = p.getFirstName();
            ln = p.getLastName();
            title = b.getTitle();
            text = c.getText();
            hasBook = a.getBooksReadOnly().size() == 1;
            hasPerson = a.getPerson() != null;
            hasComment = a.getCommentBooksReadOnly().size() == 1;
            t.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t.rollback();
        }
        assertTrue("test".equals(fn));
        assertTrue("test".equals(ln));
        assertTrue("test".equals(title));
        assertTrue("test".equals(text));
        assertTrue(hasPerson && hasBook && hasComment);
    }
}
