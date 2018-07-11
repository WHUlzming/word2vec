    public static void setProgressIndeterminate(final boolean indeterminate) {

        if (MainFrame.progressFrame != null) {

            SwingUtilities.invokeLater(new Runnable() {



                @Override

                public void run() {

                    MainFrame.progressFrame.progressBar.setIndeterminate(indeterminate);

                }

            });

        }

    }
