            cm = new Menu(res.getString("nicklist.popup.operators"));

            cm.addActionListener(this);

            mi = new MenuItem(res.getString("nicklist.popup.kick"));

            mi.setActionCommand("KICK");

            cm.add(mi);

            mi = new MenuItem(res.getString("nicklist.popup.kban"));

            mi.setActionCommand("KBAN");

            cm.add(mi);

            cm.addSeparator();

            mi = new MenuItem(res.getString("nicklist.popup.ban"));

            mi.setActionCommand("MODE {1} +b {0}");

            cm.add(mi);

            mi = new MenuItem(res.getString("nicklist.popup.unban"));

            mi.setActionCommand("MODE {1} -b {0}");

            cm.add(mi);

            cm.addSeparator();

            mi = new MenuItem(res.getString("nicklist.popup.admin"));

            mi.setActionCommand("MODE {1} +a {0}");

            cm.add(mi);

            mi = new MenuItem(res.getString("nicklist.popup.except"));

            mi.setActionCommand("MODE {1} +e {0}");

            cm.add(mi);

            mi = new MenuItem(res.getString("nicklist.popup.invite"));

            mi.setActionCommand("MODE {1} +I {0}");

            cm.add(mi);

            cm.addSeparator();

            mi = new MenuItem(res.getString("nicklist.popup.voice"));

            mi.setActionCommand("MODE {1} +v {0}");

            cm.add(mi);

            mi = new MenuItem(res.getString("nicklist.popup.unvoice"));

            mi.setActionCommand("MODE {1} -v {0}");

            cm.add(mi);
