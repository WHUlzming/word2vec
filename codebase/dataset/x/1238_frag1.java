    public static final LispObject number(BigInteger numerator, BigInteger denominator) throws ConditionThrowable {

        if (denominator.signum() == 0) error(new DivisionByZero());

        if (denominator.signum() < 0) {

            numerator = numerator.negate();

            denominator = denominator.negate();

        }

        BigInteger gcd = numerator.gcd(denominator);

        if (!gcd.equals(BigInteger.ONE)) {

            numerator = numerator.divide(gcd);

            denominator = denominator.divide(gcd);

        }

        if (denominator.equals(BigInteger.ONE)) return number(numerator); else return new Ratio(numerator, denominator);

    }
