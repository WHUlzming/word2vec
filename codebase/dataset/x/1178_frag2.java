    ArrayList new41() {

        @SuppressWarnings("hiding") ArrayList nodeList = new ArrayList();

        @SuppressWarnings("unused") ArrayList nodeArrayList1 = pop();

        PParamValue pparamvalueNode1;

        {

            PValue pvalueNode2;

            pvalueNode2 = (PValue) nodeArrayList1.get(0);

            pparamvalueNode1 = new AParamValue(pvalueNode2);

        }

        nodeList.add(pparamvalueNode1);

        return nodeList;

    }
