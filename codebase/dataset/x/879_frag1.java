        public void actionPerformed(ActionEvent evt) {

            View view = getView(evt);

            Buffer buffer = view.getBuffer();

            String selection = view.getTextArea().getSelectedText();

            if (selection == null) {

                GUIUtilities.error(view, "infoviewer.error.noselection", null);

                return;

            }

            URL u;

            try {

                u = new URL(selection);

            } catch (java.net.MalformedURLException e) {

                GUIUtilities.error(view, "infoviewer.error.badurl", new String[] { selection });

                return;

            }

            sendURL(u, getView(evt));

        }
