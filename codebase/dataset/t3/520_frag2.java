    public Object getValueAt(int pRow, String pColumn) {

        TA827VO vo = fTableData[fSortOrder[pRow]];

        if ("id".equals(pColumn)) return new Long(vo.getId());

        if ("type".equals(pColumn)) return new Long(vo.getType());

        if ("salaryPayment".equals(pColumn)) return vo.getSalaryPayment();

        if ("referenceNumber".equals(pColumn)) return vo.getReferenceNumber();

        if ("debitAccountID".equals(pColumn)) return new Long(vo.getDebitAccountID());

        if ("principalID".equals(pColumn)) return new Long(vo.getPrincipalID());

        if ("currencyID".equals(pColumn)) return new Short(vo.getCurrencyID());

        if ("amount".equals(pColumn)) return vo.getAmount();

        if ("creationDate".equals(pColumn)) return vo.getCreationDate();

        if ("dueDate".equals(pColumn)) return vo.getDueDate();

        if ("bankAccountID".equals(pColumn)) return new Long(vo.getBankAccountID());

        if ("postAccountID".equals(pColumn)) return new Long(vo.getPostAccountID());

        if ("recipientID".equals(pColumn)) return new Long(vo.getRecipientID());

        if ("beneficiaryID".equals(pColumn)) return new Long(vo.getBeneficiaryID());

        if ("message4x28".equals(pColumn)) return vo.getMessage4x28();

        if ("ordererID".equals(pColumn)) return new Long(vo.getOrdererID());

        return null;

    }
