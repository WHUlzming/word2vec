        URL webXml = getClass().getResource("/eclipse/web.xml");

        try {

            copyFile(webXml, new File(webInfDir, "web.xml"));

        } catch (IOException x) {

            throw new MojoExecutionException("Can't copy web.xml", x);

        }

        URL launchIni = getClass().getResource("/eclipse/launch.ini");
