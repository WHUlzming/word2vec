    public NotificationChain basicSetUniqueIdVar(DynamicValue newUniqueIdVar, NotificationChain msgs) {

        DynamicValue oldUniqueIdVar = uniqueIdVar;

        uniqueIdVar = newUniqueIdVar;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.GET_CALL_INFO__UNIQUE_ID_VAR, oldUniqueIdVar, newUniqueIdVar);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public void setUniqueIdVar(DynamicValue newUniqueIdVar) {

        if (newUniqueIdVar != uniqueIdVar) {

            NotificationChain msgs = null;

            if (uniqueIdVar != null) msgs = ((InternalEObject) uniqueIdVar).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.GET_CALL_INFO__UNIQUE_ID_VAR, null, msgs);

            if (newUniqueIdVar != null) msgs = ((InternalEObject) newUniqueIdVar).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.GET_CALL_INFO__UNIQUE_ID_VAR, null, msgs);

            msgs = basicSetUniqueIdVar(newUniqueIdVar, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.GET_CALL_INFO__UNIQUE_ID_VAR, newUniqueIdVar, newUniqueIdVar));

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public DynamicValue getAni2Var() {

        return ani2Var;

    }



    /**

	 * <!-- begin-user-doc --> <!-- end-user-doc -->

	 * @generated

	 */

    public NotificationChain basicSetAni2Var(DynamicValue newAni2Var, NotificationChain msgs) {
