        URL servletBridge = getClass().getResource("/eclipse/servletbridge.jar");

        try {

            copyFile(servletBridge, new File(libDir, "servletbridge.jar"));

        } catch (IOException x) {

            throw new MojoExecutionException("Can't copy servletbridge.jar", x);

        }

        InputStream in = getClass().getResourceAsStream("/eclipse/bundles.list");
