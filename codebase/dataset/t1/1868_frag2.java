                if (isTextNode(n)) {

                    StringBuffer sb = new StringBuffer(n.getNodeValue());

                    for (Node nn = n.getNextSibling(); isTextNode(nn); nn = nn.getNextSibling()) {

                        sb.append(nn.getNodeValue());

                    }
