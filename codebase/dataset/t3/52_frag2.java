    private JMenuItem getRemoveGraphMenuItem() {

        if (removeGraphMenuItem == null) {

            removeGraphMenuItem = new JMenuItem();

            removeGraphMenuItem.setText("Remove Graph");

            removeGraphMenuItem.addActionListener(this);

        }

        return removeGraphMenuItem;

    }
