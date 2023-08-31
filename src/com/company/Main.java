package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);

        int choose = 0;
        String message = "";

        System.out.println("==Статистическая обработка текста==");

        do {
            System.out.println("Выберите один из способов ввода текста: ");
            System.out.println("1 - Ввод с клавиатуры;");
            System.out.println("2 - Чтение файла input.txt (в директории проекта)");
            choose = scanner.nextInt();
            System.out.println();
        } while (choose != 1 && choose != 2);

        switch (choose) {
            case 1:
                System.out.println("Введите сообщение с помощью клавиатуры: ");
                message = scanner2.nextLine();
                break;

            case 2:
                System.out.println("Чтение из файла input.txt");
                File file = new File("input.txt");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        message += line;
                    }
                } catch (IOException exception) {
                    System.out.println("Что-то пошло не так.");
                }
                break;
        }
        System.out.println();

        System.out.println("Сообщение: ");
        System.out.println(message);
        System.out.println();

        char[] char_message = message.toCharArray();       //преобразую строку в символьный массив
        int count_of_different_sym = 0, count_of_this_symbol = 0;
        boolean isAnother = false;
        ArrayList<Symbol> Symbols = new ArrayList<Symbol>();       //создаю список объектов (букв)

        for (int i = 0; i < char_message.length; i++)       //подсчет количества различных символов в строке и заполнение списка
        {
            isAnother = true;
            count_of_this_symbol = 1;

            for (int j = 0; j < i; j++) {

                if (char_message[j] == char_message[i]) {
                    isAnother = false;
                }
            }

            if (isAnother) {
                count_of_different_sym++;
                for (int k = 0; k < char_message.length; k++)
                {
                    if (char_message[k] == char_message[i] && k!=i)
                        count_of_this_symbol++;
                }
                Symbols.add(new Symbol(char_message[i], count_of_this_symbol));
            }

        }

        Collections.sort(Symbols, new CompareFreq());                            //сортировка списка символов
        
        do {
	        System.out.println("Выберите метод оптимального кодирования:");
	        System.out.println("1 - Метод Шеннона-Фано");
	        System.out.println("2 - Метод Хаффмена");
	        choose = scanner.nextInt();
	        System.out.println();
        } while (choose != 1 && choose != 2);
        
        switch (choose)
        {
        	case (1) :
        		System.out.println("Выбран метод оптимального кодирования Шеннона-Фано.");
        		recurse_Shannon(Symbols, 0, Symbols.size() - 1);
        		break;

        	case (2):
        		System.out.println("Выбран метод оптимального кодирования Хаффмена.");
                ArrayList<Symbol> hafSymbols = new ArrayList<Symbol>();     //создаю копию листа, чтоб превратить его в дерево
                for (Symbol obj: Symbols)
                {
                    Symbol testobj = obj;
                    hafSymbols.add(testobj);                                    //копирую все значения в новый лист
                }
                Symbol HafAnswer = recurse_Haffman(hafSymbols);

                for (Symbol obj : Symbols)
                {
                    recurse_give(obj.symbol, HafAnswer, "");
                }
        		break;
        }

        System.out.println("символ | частота | относительная частота | код");

        for (Symbol sym : Symbols)
        {
            sym.relativeFrequency = (double) sym.frequency / char_message.length;
            System.out.print("   " + sym.symbol + "   |   " + sym.frequency + "     |  " );
            System.out.printf("%.5f", sym.relativeFrequency);
            System.out.println(" | " + sym.code);
        }

        System.out.println("Количество разных символов: " + count_of_different_sym);

        double entropyX = 0;

        for (Symbol sym: Symbols)
        {
            entropyX += sym.relativeFrequency * log2(sym.relativeFrequency);                 //подсчет средней энтропии одного символа
        }
        entropyX *= -1;
        System.out.println("Энтропия H: " + entropyX);

        System.out.println();

        System.out.println("Введите сообщение, которое хотите закодировать: ");
        char [] input_message = scanner2.nextLine().toCharArray();

        for (char sym: input_message)
        {
            boolean isHave = false;
            for (Symbol obj: Symbols)
            {
                if (sym == obj.symbol) {
                    isHave = true;
                    System.out.print(obj.code + " ");
                    break;
                }
            }

            if (!isHave)
            System.out.print("(NO) ");
        }

        System.out.println();
    }

    public static double log2(double x)
    {
        return Math.log(x) / Math.log(2);
    }
    
    public static void recurse_Shannon( ArrayList <Symbol> Symbols, int down_border, int up_border) 
    {
    	int part1 = 0;
    	int part2 = 0;
    	int idx = down_border - 1;
    	do
    	{
    		idx++;
    		part1 += Symbols.get(idx).frequency;
    		part2 = 0;
    		for (int j = idx + 1; j <= up_border; j++) {
                part2 += Symbols.get(j).frequency;
            }
    	} while (part1 < part2);
    	
    	for (int i = down_border; i <= idx; i++)
    	{
            if (down_border != up_border)
    			Symbols.get(i).code += "0";			//расставляем нули первой половине
    	}
    	
    	for (int j = idx + 1; j <= up_border; j++)
    	{
    			Symbols.get(j).code += "1";			//расставляем единички второй половине
    	}

        System.out.println( "КУСОК : " + down_border + ":" + idx + ":" + up_border);
        System.out.println("суммы половинок:  " + part1  + " " + part2);

        if (up_border - down_border > 1) {
            recurse_Shannon(Symbols, down_border, idx);
            recurse_Shannon(Symbols, idx + 1, up_border);
        }

    }

    public static Symbol recurse_Haffman(ArrayList <Symbol> Symbols)
    {
        while (Symbols.size() > 1) {
            Symbols.add(new Symbol(Symbols.get(Symbols.size() - 2), Symbols.get(Symbols.size() - 1)));      //добавляю новый узел
            Symbols.get(Symbols.size() - 1).refSymbol1 = Symbols.get(Symbols.size() - 2);
            Symbols.get(Symbols.size() - 1).refSymbol2 = Symbols.get(Symbols.size() - 3);

            Symbols.get(Symbols.size() - 1).refSymbol1.branch = "1";        //добавляю на веточку 1
            Symbols.get(Symbols.size() - 1).refSymbol2.branch = "0";        //добавляю на веточку 0

            Symbols.remove(Symbols.size() - 2);     //удаляю последний объект
            Symbols.remove(Symbols.size() - 2);     //удаляю предпоследний объект

            Collections.sort(Symbols, new CompareFreq());      //сортирую
        }

        return Symbols.get(0);
    }

    public static void recurse_give(char symbol, Symbol node, String symCode)
    {
        symCode += node.branch;
        if (node.symbol == symbol)
        {
            node.code = symCode;
        }

        if (node.refSymbol1 != null) {
            recurse_give(symbol, node.refSymbol1, symCode);
        }

        if (node.refSymbol2 != null) {
            recurse_give(symbol, node.refSymbol2, symCode);
        }
    }
}

