    public boolean eIsSet(int featureID) {

        switch(featureID) {

            case ActionstepPackage.SAY_NUMBER__CALL1:

                return call1 != null;

            case ActionstepPackage.SAY_NUMBER__ESCAPE_DIGITS:

                return ESCAPE_DIGITS_EDEFAULT == null ? escapeDigits != null : !ESCAPE_DIGITS_EDEFAULT.equals(escapeDigits);

            case ActionstepPackage.SAY_NUMBER__NUMBER:

                return number != null;

        }

        return super.eIsSet(featureID);

    }
