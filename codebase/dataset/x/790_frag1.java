    public PropertyChangeSupport getPropertyChangeSupport() {

        if (propertyChangeSupport == null) {

            propertyChangeSupport = new PropertyChangeSupport(this);

        }

        return propertyChangeSupport;

    }
