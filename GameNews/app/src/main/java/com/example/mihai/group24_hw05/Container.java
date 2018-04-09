package com.example.mihai.group24_hw05;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import static com.example.mihai.hw3_group24.MainActivity.buffer;

/**
 * Created by mihai on 2/2/17.
 */

public final class Container {
    static BufferedReader buffer;
    private Container(){}
    public static int testNull(String s){

        if (s.trim().equals("") || s==null) {
            return 1;
        }
        return 0;
    }

    public static int existSymbols(String s){
        Pattern pattern = Pattern.compile("[^a-zA-Z/\\s]");
        Matcher matcher = pattern.matcher(s.trim());

        List<String> listMatches = new ArrayList<String>();
        if (!s.trim().equals("")) {
            while (matcher.find()) {
                listMatches.add(matcher.group());
            }
        }

        return listMatches.size();
    }


    public static int countWord(String s){
        String[] words = s.split("[\\W\\s]+");
        return words.length;
    }


    /**
     * to be used by the findAndCountWords method
     * it gives back a list of words and the frequency of each word as a number
     * @param s
     * @return
     */
    public static Map<String, Integer> sameWord(String s){
        String[] words = s.split("[\\W\\s]+");
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (String word: words) {
            if (counts.containsKey(word)) {
                counts.put(word, counts.get(word) + 1);
            } else {
                counts.put(word, 1);
            }
        }
        return counts;
    }

    /**
     * Given a list of words, this method counts their frequency in the text (the buffer from main)
     * @param list
     * @return
     */

    public static Map<String, Integer> findAndCountWords(Map<String, Integer> list){
        String text = new String(String.valueOf(buffer)); //this is the buffer from Main where the text is stored
       // Map<String, Integer> counts = new HashMap<>();

        //since the text is too big we split it in substrings of roughly 1000 characters without loosing words
        List<String> strings = new ArrayList<>();

        int index = 0;
        while (index < text.length()) {
            strings.add(new String(String.valueOf(Arrays.copyOfRange(new BufferedReader[]{buffer}, index, Math.min(index + 500,text.length())))));
            index += 500;
        }

        //counting word matches in each substring
        for (String s: strings) {
            StringTokenizer st = new StringTokenizer(s);

            while (st.hasMoreTokens()) {
                String word = st.nextToken();
                if (list.keySet().contains(word)) {
                    list.put(word, list.get(word) + 1);
                }

            }
        }
        Log.d("demo", "The array with counts: "+list);
        return list;
    }

    public static int checkValidString(EditText et, Activity main) {
        String currentWord=et.getText().toString().trim();
        if (Container.testNull(currentWord) != 0) {
            et.setText("");
            Toast.makeText(main, "Existing boxes cannot be empty!", Toast.LENGTH_LONG).show();
            return 1;
        } else if (Container.existSymbols(currentWord)!=0){
            et.setText("");
            Toast.makeText(main, et.getText().toString()+" Cannot input a non letter character!", Toast.LENGTH_LONG).show();
            return 1;
        }else if (Container.countWord(currentWord)>1){
            et.setText("");
            Toast.makeText(main, et.getText().toString()+" Cannot input more than one word in one box!", Toast.LENGTH_LONG).show();
            return 1;
        }
        Log.d("demo", "Words "+(Container.countWord(currentWord)));

    return 0;

    }


    /**

    public static Map<String, Integer> findAndCountWords(Map<String, Integer> list){
        //String text = new String(buffer); //this is the buffer from Main where the text is stored
        Map<String, Integer> counts = new HashMap<>();

        //since the text is too big we split it in substrings of roughly 1000 characters without loosing words
        List<String> strings = new ArrayList<>();
        int index = 0;
        int index_end=0;
        while (index < buffer.length) {
            index_end =index+1000;
            if(index_end!=0 || !(index_end>=buffer.length-1)) {
                while (!Character.isWhitespace((char)buffer[index_end])){
                    index_end++;
                    Log.d("demo", "Passes");
                }
            }

            strings.add(new String(Arrays.copyOfRange(buffer, index, Math.min(index_end, buffer.length-1))));
            //Log.d("demo", "current substring : " + new String(Arrays.copyOfRange(buffer, index, Math.min(index_end,buffer.length))));
            index += 1000;
            if(index!=0 || !(index>=buffer.length-1)) {
                while (buffer[index]!=' '){
                    index++;
                }
            }

        }

        //counting word matches in each substring
        for (String s: strings) {
            StringTokenizer st = new StringTokenizer(s);

            while (st.hasMoreTokens()) {
                String word = st.nextToken();
                if (list.keySet().contains(word)) {
                    list.put(word, list.get(word) + 1);
                }

            }
        }
        return counts;
    }
*/

}
