                urls = loader.getResources("META-INF/org.virbo.datasource.DataSourceFactory.extensions");

            }

            while (urls.hasMoreElements()) {

                URL url = urls.nextElement();

                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                String s = reader.readLine();

                while (s != null) {
