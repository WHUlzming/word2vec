    @Override

    public Message clone() throws CloneNotSupportedException {

        Message m = null;

        try {

            m = (Message) super.clone();

            m.message = message;

            m.properties = properties.clone();

        } catch (CloneNotSupportedException e) {

            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, e.toString(), e);

        }

        return m;

    }
