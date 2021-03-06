    public static void configure() {

        if (configureFrame != null) {

            configureFrame.setVisible(true);

            return;

        }

        final JComponent ui = AudioServerUIServices.createServerUI(server, serverConfig);

        if (ui == null) {

            return;

        }

        configureFrame = new JFrame();

        configureFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configureFrame.setAlwaysOnTop(true);

        configureFrame.setContentPane(ui);

        configureFrame.pack();

        configureFrame.setVisible(true);

    }
