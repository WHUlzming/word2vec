            String path = ST.nextToken();

            if (!path.startsWith("\"")) {

                cur_client.sendFromBot("Filename must be enclosed in quotes.");

                return;

            }

            String bla = path;

            while (!bla.endsWith("\"")) {

                if (!ST.hasMoreTokens()) {

                    cur_client.sendFromBot("Filename must be enclosed in quotes.");

                    return;

                }

                bla = ST.nextToken();

                path += " " + bla;

            }

            path = path.substring(1, path.length() - 1);

            if (Main.listaBanate.loadFile(path) == true) cur_client.sendFromBot("Successfully loaded."); else cur_client.sendFromBot("File access error.");
