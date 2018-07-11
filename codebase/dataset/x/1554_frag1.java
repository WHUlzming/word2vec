    public NotificationChain basicSetExtension(DynamicValue newExtension, NotificationChain msgs) {

        DynamicValue oldExtension = extension;

        extension = newExtension;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.EXTENSION_TRANSFER__EXTENSION, oldExtension, newExtension);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public void setExtension(DynamicValue newExtension) {

        if (newExtension != extension) {

            NotificationChain msgs = null;

            if (extension != null) msgs = ((InternalEObject) extension).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.EXTENSION_TRANSFER__EXTENSION, null, msgs);

            if (newExtension != null) msgs = ((InternalEObject) newExtension).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.EXTENSION_TRANSFER__EXTENSION, null, msgs);

            msgs = basicSetExtension(newExtension, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.EXTENSION_TRANSFER__EXTENSION, newExtension, newExtension));

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public int getPriority() {

        return priority;

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public void setPriority(int newPriority) {
