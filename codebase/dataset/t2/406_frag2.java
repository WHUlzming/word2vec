        for (int ParentVector = 0; ParentVector < NumParentSolutions; ParentVector++) {

            if (ParentMatrix[NumVertices][ParentVector] == 0) {

                for (int VectorElement = 0; VectorElement < NumVertices; VectorElement++) {

                    SolutionVector[VectorElement] = ParentMatrix[VectorElement][ParentVector];

                }

                ParentMatrix[NumVertices][ParentVector] = CalculateTreeWidth(Graph, SolutionVector, 0);

                if ((lokalMinTreeWidth == 0) || ParentMatrix[NumVertices][ParentVector] < lokalMinTreeWidth) {

                    lokalMinTreeWidth = ParentMatrix[NumVertices][ParentVector];

                }

            }

        }

        for (int ChildrenVector = 0; ChildrenVector < NumChildrenSolutions; ChildrenVector++) {
