package com.company;

public class Symbol {

    char symbol;
    String doublesymbol;
    int frequency;
    double relativeFrequency;
    String code = "";
    String branch = "";
    Symbol refSymbol1 = null;
    Symbol refSymbol2 = null;

    Symbol(char symbol, int frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
        code = "";
    }

    Symbol(String doublesymbol, int frequency) {
        this.doublesymbol = doublesymbol;
        this.frequency = frequency;
        code = "";
    }

    Symbol(Symbol sym1, Symbol sym2)
    {
        this.frequency = sym1.frequency + sym2.frequency;
        refSymbol1 = sym1;
        refSymbol2 = sym2;
    }

}

