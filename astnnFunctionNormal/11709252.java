class BackupThread extends Thread {
        @SuppressWarnings("unchecked")
        private final void batchSaveOrUpdate(final Collection<IInfoSpaceItem> objects) throws CannotConnectToDatabaseException {
            if (objects != null && !objects.isEmpty()) {
                final Session s = ModelManager.getInstance().getCurrentSession();
                Transaction tx = null;
                try {
                    tx = s.beginTransaction();
                    final String oldCacheMode = s.getCacheMode().toString();
                    if (s.getCacheMode() != CacheMode.IGNORE) {
                        s.setCacheMode(CacheMode.IGNORE);
                    }
                    s.createSQLQuery("SET foreign_key_checks=0;").executeUpdate();
                    int itemCount = 0;
                    float revCount = 0;
                    for (final IInfoSpaceItem obj : objects) {
                        if (obj instanceof IRevisionElement) {
                            revCount++;
                        }
                        s.saveOrUpdate(obj);
                        if (++itemCount % BATCH_SIZE == 0) {
                            s.flush();
                            s.clear();
                        }
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Fraction of RevisionElement entities: " + ((revCount * 100) / BATCH_SIZE) + "%");
                    }
                    s.createSQLQuery("SET foreign_key_checks=1;").executeUpdate();
                    s.setCacheMode(CacheMode.parse(oldCacheMode));
                    s.clear();
                    tx.commit();
                    itemsStoredCount += objects.size();
                } catch (final HibernateException he) {
                    if (tx == null) {
                        if (LOGGER.isErrorEnabled()) {
                            LOGGER.error("Failed to create transaction.");
                        }
                        throw he;
                    } else {
                        tx.rollback();
                        String exceptionType = null;
                        String problemType = null;
                        if (he instanceof PropertyValueException) {
                            exceptionType = "PropertyValueException";
                        } else if (he instanceof GenericJDBCException) {
                            exceptionType = "GenericJDBCException";
                            final GenericJDBCException gje = (GenericJDBCException) he;
                            if (gje.getErrorCode() == 1366) {
                                final String msg = gje.getMessage();
                                if (msg != null) {
                                    problemType = msg.substring(msg.indexOf("[") + 1, msg.indexOf("]"));
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.debug("ProblemType is: " + problemType);
                                    }
                                }
                            }
                        } else if (he.getCause() instanceof BatchUpdateException) {
                            exceptionType = "BatchUpdateException";
                        } else {
                            if (LOGGER.isErrorEnabled()) {
                                LOGGER.error("Failed to batch insert " + objects.size() + " items.");
                            }
                            throw he;
                        }
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Handling " + exceptionType);
                        }
                        if (s != null && s.isOpen()) {
                            s.close();
                        }
                        final int size = objects.size();
                        if (size > 1 && !this.saveItemSplitted) {
                            final List<IInfoSpaceItem> basicItems = new ArrayList<IInfoSpaceItem>();
                            final List<IInfoSpaceItem> skippedItems = new ArrayList<IInfoSpaceItem>();
                            final List<IInfoSpaceItem> fundamentalRelations = new ArrayList<IInfoSpaceItem>();
                            entityLoop: for (final IInfoSpaceItem obj : objects) {
                                if (obj instanceof IBasicItem) {
                                    if (exceptionType.equals("GenericJDBCException")) {
                                        if (obj.getClass().getCanonicalName().equals(problemType)) {
                                            String str = null;
                                            if (obj instanceof IContentElement) {
                                                str = ((IContentElement) obj).getTitle();
                                            } else {
                                                str = ((IActor) obj).getName();
                                            }
                                            for (int i = 0; i < str.length(); i++) {
                                                if (str.charAt(i) >= 0x10000) {
                                                    if (LOGGER.isInfoEnabled()) {
                                                        LOGGER.info("Found disallowed character in title / name of entity. Skipping item.");
                                                    }
                                                    skippedItems.add(obj);
                                                    continue entityLoop;
                                                }
                                            }
                                        }
                                    }
                                    basicItems.add(obj);
                                } else {
                                    fundamentalRelations.add(obj);
                                }
                            }
                            this.saveItemSplitted = true;
                            if (!skippedItems.isEmpty() && !fundamentalRelations.isEmpty()) {
                                if (LOGGER.isInfoEnabled()) {
                                    LOGGER.info("Checking relational entities for skipped items.");
                                }
                                final Iterator<IInfoSpaceItem> iter = fundamentalRelations.iterator();
                                while (iter.hasNext()) {
                                    final IInfoSpaceItem isit = iter.next();
                                    if (isit instanceof IActorContentElementRelation) {
                                        final IActorContentElementRelation acer = (IActorContentElementRelation) isit;
                                        if (skippedItems.contains(acer.getActor()) || skippedItems.contains(acer.getContentElement())) {
                                            if (LOGGER.isInfoEnabled()) {
                                                LOGGER.info("Skipping entity of type " + isit.getType().getCanonicalName());
                                            }
                                            iter.remove();
                                        }
                                    } else if (isit instanceof IContextRelation) {
                                        final IContextRelation ctx = (IContextRelation) isit;
                                        if (skippedItems.contains(ctx.getSource()) || skippedItems.contains(ctx.getTarget())) {
                                            if (LOGGER.isInfoEnabled()) {
                                                LOGGER.info("Skipping entity of type " + isit.getType().getCanonicalName());
                                            }
                                            iter.remove();
                                        }
                                    } else {
                                        final IInteractionRelation ia = (IInteractionRelation) isit;
                                        if (skippedItems.contains(ia.getSource()) || skippedItems.contains(ia.getTarget())) {
                                            if (LOGGER.isInfoEnabled()) {
                                                LOGGER.info("Skipping entity of type " + isit.getType().getCanonicalName());
                                            }
                                            iter.remove();
                                        }
                                    }
                                }
                            }
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Retrying to store " + basicItems.size() + " basic items first.");
                            }
                            this.batchSaveOrUpdate(basicItems);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Now storing " + fundamentalRelations.size() + " fundamental relations.");
                            }
                            this.batchSaveOrUpdate(fundamentalRelations);
                        } else if (size > 1) {
                            final int halfSize = size / 2;
                            final List<IInfoSpaceItem> chunk1 = new ArrayList<IInfoSpaceItem>(halfSize);
                            final List<IInfoSpaceItem> chunk2 = new ArrayList<IInfoSpaceItem>(halfSize);
                            int itemCount = 0;
                            boolean firstChunkSuccess = false;
                            for (final IInfoSpaceItem obj : objects) {
                                if (!firstChunkSuccess) {
                                    if (itemCount++ <= halfSize) {
                                        chunk1.add(obj);
                                    } else {
                                        this.batchSaveOrUpdate(chunk1);
                                        if (LOGGER.isDebugEnabled()) {
                                            LOGGER.debug("Successfully inserted chunk 1 with " + chunk1.size() + " items.");
                                        }
                                        firstChunkSuccess = true;
                                    }
                                } else {
                                    chunk2.add(obj);
                                }
                            }
                            if (!chunk2.isEmpty()) {
                                this.batchSaveOrUpdate(chunk2);
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("Successfully inserted chunk 2 with " + chunk2.size() + " items.");
                                }
                            }
                        } else {
                            if (LOGGER.isErrorEnabled()) {
                                if (objects.size() > 1) {
                                    LOGGER.error("There is an item that could not be inserted.");
                                } else {
                                    LOGGER.error("There are items that could not be inserted.");
                                }
                                for (final IInfoSpaceItem obj : objects) {
                                    LOGGER.error("Item of type " + obj.getClass().getSimpleName());
                                    if (obj instanceof IActor) {
                                        LOGGER.error("The name is " + ((IActor) obj).getName());
                                    } else if (obj instanceof IContentElement) {
                                        LOGGER.error("The title is " + ((IContentElement) obj).getTitle());
                                    } else if (obj instanceof IContextRelation) {
                                        final IContextRelation ctx = (IContextRelation) obj;
                                        LOGGER.error("Connects serialID " + ctx.getSource().getSerialId() + " as source and serialID " + ctx.getTarget().getSerialId() + " as target");
                                    } else if (obj instanceof IInteractionRelation) {
                                        final IInteractionRelation ia = (IInteractionRelation) obj;
                                        LOGGER.error("Connects serialID " + ia.getSource().getSerialId() + " as source and serialID " + ia.getTarget().getSerialId() + " as target");
                                    } else if (obj instanceof IActorContentElementRelation) {
                                        final IActorContentElementRelation acer = (IActorContentElementRelation) obj;
                                        LOGGER.error("Connects serialID " + acer.getActor().getSerialId() + " as actor and serialID " + acer.getContentElement().getSerialId() + " as content element");
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    if (s != null && s.isOpen()) {
                        s.close();
                    }
                }
            }
        }
}
