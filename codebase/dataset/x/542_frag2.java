    private static Object message0(Object[] _p, boolean _eval) {

        Object r = null;

        FooLib.checkClassArgument(FooCategory.class, _p[0], 0);

        FooCategory c = (FooCategory) _p[0];

        switch(_p.length) {

            case 1:

                FooList l = new FooList();

                for (Enumeration e = c.messages_.keys(); e.hasMoreElements(); ) l.add(e.nextElement());

                r = l;

                break;

            case 2:

                r = c.getMessage(_p[1].toString());

                break;

            case 3:

                String name = _p[1].toString();

                Object obj = FooLib.eval(_p[2]);

                if (obj instanceof FooMessage) c.setMessage((FooMessage) obj); else if (obj instanceof Field) c.setMessage(name, (Field) obj); else if (obj instanceof Method) c.setMessage(name, (Method) obj); else if (obj instanceof String) c.setMessage(name, (String) obj); else throw new RuntimeException("invalid argument:" + FooLib.toString(_p[2]));

                r = c;

                break;

            default:

                String[] argnames = null;

                if (_p[2] instanceof FooList) {

                    FooList a = (FooList) _p[2];

                    try {

                        int la = a.length();

                        argnames = new String[la];

                        for (int i = 0; i < la; i++) argnames[i] = ((FooSymbol) a.at(i)).getName();

                    } catch (Exception ex) {

                        throw new RuntimeException("invalid list of arguments:" + FooLib.toString(a));

                    }

                } else FooLib.checkClassArgument(Integer.class, _p[2], 2);

                Object[] q = new Object[_p.length - 3];

                for (int i = 3; i < _p.length; i++) q[i - 3] = _p[i];

                c.setMessage(_p[1].toString(), q, argnames, _eval);

                r = c;

                break;

        }

        return r;

    }