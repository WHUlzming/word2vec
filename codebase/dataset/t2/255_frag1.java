    public boolean eIsSet(int featureID) {

        switch(featureID) {

            case ActionstepPackage.GET_FULL_VARIABLE__CALL1:

                return call1 != null;

            case ActionstepPackage.GET_FULL_VARIABLE__VARIABLE:

                return VARIABLE_EDEFAULT == null ? variable != null : !VARIABLE_EDEFAULT.equals(variable);

            case ActionstepPackage.GET_FULL_VARIABLE__ASSIGN_TO_VAR:

                return assignToVar != null;

        }

        return super.eIsSet(featureID);

    }
