class BackupThread extends Thread {
    private void importGroupMembership(File log) {
        FileOutputStream _log = null;
        try {
            EntryManager _em = new EntryManager(this._local_c);
            GroupManager _gm = new GroupManager(this._local_c);
            UserManager _um = new UserManager(this._local_c);
            _log = new FileOutputStream(log, true);
            this._eb.setScope(LDAPConnection.ONE_SCOPE);
            Query _q = new Query();
            _q.addCondition("objectclass", "top", Query.EXACT);
            write(_log, "Reading remote base entries .. ");
            List<Entry> _containers = this._eb.search(_q);
            writeLine(_log, "done");
            for (Entry _ec : _containers) {
                writeLine(_log, "Processing container: " + _ec.getID());
                this._eb.setScope(LDAPConnection.SUBTREE_SCOPE);
                _q = new Query();
                _q.addCondition("objectclass", "group", Query.EXACT);
                if (_ec.hasAttribute("dc")) {
                    _q.addCondition("dc", String.valueOf(_ec.getAttribute("dc")[0]), Query.BRANCH);
                } else if (_ec.hasAttribute("ou")) {
                    _q.addCondition("ou", String.valueOf(_ec.getAttribute("ou")[0]), Query.BRANCH);
                } else if (_ec.hasAttribute("cn")) {
                    _q.addCondition("cn", String.valueOf(_ec.getAttribute("cn")[0]), Query.BRANCH);
                }
                write(_log, "Reading entries from remote directory .. ");
                List<Entry> _results = this._eb.search(_q);
                writeLine(_log, "done");
                for (Entry _g : _results) {
                    Entry _g_tmp = null;
                    List<String> _values_dn = new ArrayList<String>();
                    List<String> _values_uid = new ArrayList<String>();
                    List<String> _values_sid = new ArrayList<String>();
                    try {
                        _g_tmp = _gm.getGroupEntry(String.valueOf(_g.getAttribute("name")[0]));
                    } catch (Exception _ex) {
                        continue;
                    }
                    writeLine(_log, "Checking group: " + _g.getAttribute("name")[0]);
                    if (_g_tmp.hasAttribute("uniqueMember") && _g_tmp.hasAttribute("memberUid") && _g_tmp.hasAttribute("sambaSIDList")) {
                        write(_log, "Verifying members .. ");
                        for (Object _dn : _g_tmp.getAttribute("uniqueMember")) {
                            Entry _e_tmp = null;
                            try {
                                _e_tmp = _em.getEntry(String.valueOf(_dn));
                            } catch (Exception _ex) {
                                continue;
                            }
                            if (_e_tmp != null) {
                                _values_dn.add(String.valueOf(_dn));
                                if (_e_tmp.hasAttribute("uid")) {
                                    _values_uid.add(String.valueOf(_e_tmp.getAttribute("uid")[0]));
                                }
                                if (_e_tmp.hasAttribute("sambaSID")) {
                                    _values_sid.add(String.valueOf(_e_tmp.getAttribute("sambaSID")[0]));
                                }
                            }
                        }
                        writeLine(_log, "done");
                    }
                    if (_g.hasAttribute("member")) {
                        for (Object chain_value : _g.getAttribute("member")) {
                            Entry _e_tmp = null;
                            try {
                                _e_tmp = this._eb.getEntry(String.valueOf(chain_value));
                            } catch (Exception _ex) {
                                continue;
                            }
                            if (_e_tmp != null) {
                                List<Object> objectclasses = Arrays.asList(_e_tmp.getAttribute("objectclass"));
                                try {
                                    if (objectclasses.contains("person") && _e_tmp.hasAttribute("sAMAccountName")) {
                                        Entry _tmp_u = _um.getUserEntry(String.valueOf(_e_tmp.getAttribute("sAMAccountName")[0]));
                                        write(_log, "Adding user member [");
                                        write(_log, String.valueOf(_e_tmp.getAttribute("sAMAccountName")[0]));
                                        write(_log, "] .. ");
                                        if (!_values_dn.contains(_tmp_u.getID())) {
                                            _values_dn.add(_tmp_u.getID());
                                            if (_tmp_u.hasAttribute("uid")) {
                                                _values_uid.add(String.valueOf(_tmp_u.getAttribute("uid")[0]));
                                            }
                                            if (_tmp_u.hasAttribute("sambaSID")) {
                                                _values_sid.add(String.valueOf(_tmp_u.getAttribute("sambaSID")[0]));
                                            }
                                            writeLine(_log, "done");
                                        } else {
                                            writeLine(_log, "already exists");
                                        }
                                    } else if (objectclasses.contains("group") && _e_tmp.hasAttribute("name")) {
                                        write(_log, "Adding group member [");
                                        write(_log, String.valueOf(_e_tmp.getAttribute("name")[0]));
                                        write(_log, "] .. ");
                                        Entry _tmp_g = _gm.getGroupEntry(String.valueOf(_e_tmp.getAttribute("name")[0]));
                                        if (!_values_dn.contains(_tmp_g.getID())) {
                                            _values_dn.add(_tmp_g.getID());
                                            writeLine(_log, "done");
                                        } else {
                                            writeLine(_log, "already exists");
                                        }
                                    }
                                } catch (Exception _ex) {
                                    writeLine(_log, "error");
                                    continue;
                                }
                            }
                        }
                    } else if (_g.hasAttribute("uniqueMember")) {
                        for (Object chain_value : _g.getAttribute("uniqueMember")) {
                            Entry _e_tmp = null;
                            try {
                                _e_tmp = this._eb.getEntry(String.valueOf(chain_value));
                            } catch (Exception _ex) {
                                continue;
                            }
                            if (_e_tmp != null) {
                                List<Object> objectclasses = Arrays.asList(_e_tmp.getAttribute("objectclass"));
                                try {
                                    if (objectclasses.contains("posixAccount") && _e_tmp.hasAttribute("uid")) {
                                        Entry _tmp_u = _um.getUserEntry(String.valueOf(_e_tmp.getAttribute("uid")[0]));
                                        write(_log, "Adding user member [");
                                        write(_log, String.valueOf(_e_tmp.getAttribute("uid")[0]));
                                        write(_log, "] .. ");
                                        if (!_values_dn.contains(_tmp_u.getID())) {
                                            _values_dn.add(_tmp_u.getID());
                                            if (_tmp_u.hasAttribute("uid")) {
                                                _values_uid.add(String.valueOf(_tmp_u.getAttribute("uid")[0]));
                                            }
                                            if (_tmp_u.hasAttribute("sambaSID")) {
                                                _values_sid.add(String.valueOf(_tmp_u.getAttribute("sambaSID")[0]));
                                            }
                                            writeLine(_log, "done");
                                        } else {
                                            writeLine(_log, "already exists");
                                        }
                                    } else if (objectclasses.contains("posixGroup") && _e_tmp.hasAttribute("cn")) {
                                        write(_log, "Adding group member [");
                                        write(_log, String.valueOf(_e_tmp.getAttribute("cn")[0]));
                                        write(_log, "] .. ");
                                        Entry _tmp_g = _gm.getGroupEntry(String.valueOf(_e_tmp.getAttribute("cn")[0]));
                                        if (!_values_dn.contains(_tmp_g.getID())) {
                                            _values_dn.add(_tmp_g.getID());
                                            writeLine(_log, "done");
                                        } else {
                                            writeLine(_log, "already exists");
                                        }
                                    }
                                } catch (Exception _ex) {
                                    writeLine(_log, "error");
                                    continue;
                                }
                            }
                        }
                    } else if (_g.hasAttribute("memberUid")) {
                        for (Object _value : _g.getAttribute("memberUid")) {
                            try {
                                Entry _tmp_u = _um.getUserEntry(String.valueOf(_value));
                                write(_log, "Adding user member [");
                                write(_log, String.valueOf(_value));
                                write(_log, "] .. ");
                                if (!_values_dn.contains(_tmp_u.getID())) {
                                    _values_dn.add(_tmp_u.getID());
                                    if (_tmp_u.hasAttribute("uid")) {
                                        _values_uid.add(String.valueOf(_tmp_u.getAttribute("uid")[0]));
                                    }
                                    if (_tmp_u.hasAttribute("sambaSID")) {
                                        _values_sid.add(String.valueOf(_tmp_u.getAttribute("sambaSID")[0]));
                                    }
                                    writeLine(_log, "done");
                                } else {
                                    writeLine(_log, "already exists");
                                }
                            } catch (Exception _ex) {
                            }
                        }
                    }
                    if (_values_dn.isEmpty() && _values_uid.isEmpty() && _values_sid.isEmpty()) {
                        continue;
                    }
                    if (!_values_dn.isEmpty()) {
                        _g_tmp.setAttribute("uniqueMember", _values_dn.toArray());
                    }
                    if (!_values_uid.isEmpty()) {
                        _g_tmp.setAttribute("memberUid", _values_uid.toArray());
                    }
                    if (_values_sid.size() > 0) {
                        _g_tmp.setAttribute("sambaSIDList", _values_sid.toArray());
                    }
                    try {
                        write(_log, "Updating group members .. ");
                        _em.updateEntry(_g_tmp);
                        writeLine(_log, "done");
                    } catch (Exception _ex) {
                        writeLine(_log, "error - " + _ex.getMessage());
                    }
                }
            }
        } catch (Exception _ex) {
            writeLine(_log, "error - " + _ex.getMessage());
        } finally {
            if (_log != null) {
                try {
                    _log.close();
                } catch (IOException _ex2) {
                }
            }
        }
    }
}
