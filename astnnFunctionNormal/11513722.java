class BackupThread extends Thread {
    public static void addToConfiguration(final AnnotationConfiguration config, final boolean enableCaching) {
        config.addAnnotatedClass(HibArc.class);
        config.addAnnotatedClass(HibArcToken.class);
        config.addAnnotatedClass(HibGraph.class);
        config.addAnnotatedClass(HibGraphListener.class);
        config.addAnnotatedClass(HibProcessListener.class);
        config.addAnnotatedClass(HibNode.class);
        config.addAnnotatedClass(HibNodeRef.class);
        config.addAnnotatedClass(HibNodeToken.class);
        config.addAnnotatedClass(HibGraphProcess.class);
        config.addAnnotatedClass(HibPropertyNode.class);
        config.addAnnotatedClass(HibCustomNodeWrapper.class);
        config.addAnnotatedClass(HibTokenSet.class);
        config.addAnnotatedClass(HibArcTokenSetMember.class);
        config.addAnnotatedClass(HibNodeTokenSetMember.class);
        config.addAnnotatedClass(HibNodeType.class);
        config.addAnnotatedClass(HibTokenSetMemberAttribute.class);
        config.addAnnotatedClass(HibExternal.class);
        if (enableCaching) {
            config.setCacheConcurrencyStrategy(HibGraph.class.getName(), "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraph.class.getName() + ".nodes", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraph.class.getName() + ".arcs", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraph.class.getName() + ".listeners", "read-write");
            config.setCacheConcurrencyStrategy(HibGraphListener.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibNode.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibNodeRef.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibArc.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibExternal.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibGraphProcess.class.getName(), "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".listeners", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".activeArcTokens", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".activeNodeTokens", "read-write");
            config.setCollectionCacheConcurrencyStrategy(HibGraphProcess.class.getName() + ".executionQueue", "read-write");
            config.setCacheConcurrencyStrategy(HibProcessListener.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibNodeToken.class.getName(), "read-write");
            config.setCacheConcurrencyStrategy(HibArcToken.class.getName(), "read-write");
        }
    }
}
