    public java.lang.Object visit(Kitsch.yyTree.top_stmt node) {

        Kitsch.yyTree.Visit elem;

        for (int x = 0; x < node.size(); x++) {

            elem = (Kitsch.yyTree.Visit) node.get(x);

            if (elem instanceof Kitsch.yyTree.FuncDecl) {

                elem.visit(this);

            }

        }

        return null;

    }
