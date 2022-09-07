class BackupThread extends Thread {
    private AttrValue rewriteUntilValid(AttrValue av, Set<AttrValue> explain_use_indexes, Map<String, AttrValue> service_specific_rewrite_rules, String default_namespace) throws ProfileServiceException {
        AttrValue result = av;
        if (av != null) {
            String av_str_val = av.getWithDefaultNamespace(default_namespace);
            if (explain_use_indexes.contains(av)) {
                log.debug("No need to rewrite, source index " + av + " is already allowed by target");
            } else {
                log.debug("Rewrite, source index " + av + " is disallowed, scanning server alternatives allowed=" + explain_use_indexes);
                boolean found = false;
                for (java.util.Iterator i = service_specific_rewrite_rules.entrySet().iterator(); ((i.hasNext()) && (!found)); ) {
                    Map.Entry e = (Map.Entry) i.next();
                    if (e.getKey().equals(av_str_val)) {
                        AttrValue new_av = (AttrValue) e.getValue();
                        log.debug("Possible rewrite: " + new_av);
                        if (explain_use_indexes.contains(new_av)) {
                            log.debug("Matched, replacing");
                            result = new_av;
                            found = true;
                        }
                    }
                }
                if (!found) {
                    log.debug("Unable to rewrite query, exception");
                    throw new ProfileServiceException("Unable to rewrite access point '" + av_str_val + "' to comply with service explain record", ERROR_QUERY);
                }
            }
        }
        return result;
    }
}
