            if (elt.hasAttributeNS(null, "onkeydown")) {

                target.addEventListener("keydown", keydownListener, false);

            }

            if (elt.hasAttributeNS(null, "onkeyup")) {

                target.addEventListener("keyup", keyupListener, false);

            }

        }

        for (Node n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
