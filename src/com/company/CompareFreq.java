package com.company;

import java.util.Comparator;

public class CompareFreq implements Comparator<Symbol>
{
    @Override
    public int compare(Symbol s1, Symbol s2)
    {
        return Integer.compare(s2.frequency, s1.frequency);
    }

}
