        captureProgressCancelButton = button;

        button = new JButton("Stop and Save");

        button.addActionListener(new ActionListener() {



            public void actionPerformed(ActionEvent e) {

                captureDone(true);

            }

        });

        captureProgressSaveButton = button;

        closePanel.setLayout(new BoxLayout(closePanel, BoxLayout.X_AXIS));
