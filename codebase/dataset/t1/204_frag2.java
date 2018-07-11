        String expected = "DATA SEGMENT\nz_0 DW 4\na_1 DW 0\nstr_1 DB 'a ='\nDATA ENDS\n" + "\nSTACK SEGMENT\nDB 200 DUP(0)\nSTACK ENDS\n\nCODE SEGMENT\n" + "ASSUME CS:CODE,DS:DATA,SS:STACK\nMAIN PROC FAR\n" + "MOV AX, DATA\nMOV DS, AX\nPUSH 10\nPOP a_1\n" + "PUSH OFFSET str_1 \nPUSH 5 \nCALL writeSTR \n" + "PUSH 0000h \nPUSH a_1\nCALL writeNUM\nCALL writeCRLF\n" + "MOV AX, 4C00H\nINT 21h\nMAIN ENDP\n\nwriteCRLF PROC NEAR\n" + "PUSH AX\nMOV AL, 0Dh\nMOV AH, 0Eh\nINT 10h\nMOV AL, 0Ah\n" + "MOV AH, 0Eh\nINT 10h\nPOP AX\nRET\nwriteCRLF ENDP\n" + "\nwriteNUM PROC NEAR\nPUSH BP\nMOV BP, SP\nSUB SP, 1\n" + "SUB SP, 6\nPUSH AX\nPUSH BX\nPUSH CX\nPUSH DX\nPUSH SI\n" + "MOV [BP-1], 00h\nMOV AX, [BP+4]\nCMP [BP+6], 0\n" + "JE comenzar\nCMP AX, 0\nJGE comenzar\nNEG AX\n" + "MOV [BP-1], 01h\ncomenzar:\nMOV BX, 10\nMOV CX, 0\n" + "MOV SI, BP\nSUB SI, 8\nproxdiv:\nDEC SI\nXOR DX,DX\n" + "DIV BX\nADD dl, 48\nMOV [SI], dl\nINC CX\nCMP AX, 0\n" + "JNZ proxdiv\nCMP [BP-1], 00h\nJZ mostrar\nDEC SI\n" + "MOV [SI], '-'\nINC CX\nmostrar:\nPUSH SI\nPUSH CX\n" + "CALL writeSTR\nPOP SI\nPOP DX\nPOP CX\nPOP BX\nPOP AX\n" + "MOV SP, BP\nPOP BP\nRET 4\nwriteNUM ENDP\n" + "\nwriteSTR PROC NEAR \nPUSH  BP\nMOV BP, SP\n" + "PUSH AX\nPUSH BX\nPUSH CX\nPUSH SI\nMOV SI, [BP+6]\n" + "MOV CX, [BP+4]\nXOR BX, BX\nloop:\nMOV AL, [SI]\n" + "MOV AH, 0Eh\nINT 10h\nINC BX\nINC SI\nCMP BX, CX\n" + "JNE loop\nPOP SI\nPOP CX\nPOP BX\nPOP AX\nPOP BP\n" + "RET 4\nwriteSTR ENDP \nCODE ENDS\n";

        String actual = "";

        Reader r = new StringReader(code);

        this.processor.proccess(r);

        Boolean status = this.processor.getStatus();

        if (status) actual = this.processor.getGeneratedCode();

        Assert.assertEquals(expected, actual);

    }
