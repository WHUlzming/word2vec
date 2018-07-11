    private void handleP2PInitEvent(P2PInitEvent event) {

        this.localNode = event.getLocalPeer();

        this.initializeTempFiles(1);

        log.debug("INIT " + this.localNode);

        try {

            event.go();

        } catch (AppiaEventException ex) {

            ex.printStackTrace();

        }

        if (this.contactNode != null) {

            try {

                InitMembershipEvent ime = new InitMembershipEvent(this.channel, Direction.DOWN, this, new Peer(this.contactNode));

                ime.go();

            } catch (AppiaEventException e) {

                e.printStackTrace();

            }

        }

    }
