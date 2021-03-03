import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

    public class Reader {
        public static void main (String args[])
            throws FileNotFoundException
            {

                ArrayList<Entry> words = new ArrayList<Entry>();

                Comparator<Entry> wordComparator = new Comparator<Entry>() {
                    public int compare(Entry o1, Entry o2) {
                        return o1.word.compareToIgnoreCase(o2.word);
                    }
                };

                Comparator<Entry> wordCountComparator = new Comparator<Entry>() {
                    public int compare(Entry o1, Entry o2) {
                        if (o1.getWordCount() < o2.getWordCount()) {
                            return -1;
                        }
                        else if ( o1.getWordCount() > o2.getWordCount()) {
                            return 1;
                        }
                        else return 0;

                    }
                };



                //import dale chall words
                ArrayList<String> DCwords = new ArrayList<String>();
                FileReader DCreader = new FileReader("DCwordlist1995.txt");
                Scanner scammer = new Scanner(DCreader);
                int hardWordCount = 0;
                int notHard = 0;
                while(scammer.hasNextLine()){
                  DCwords.add(scammer.nextLine().trim());
                }






                Scanner console = new Scanner(System.in);
                System.out.print("Input file: ");
                String inputFileName = console.next();
                FileReader reader = new FileReader(inputFileName);
                Scanner in = new Scanner(reader);
                int lineNumber = 1;
                int sentences = 0;
                int qCount = 0;
                int index = 0;
                while (in.hasNextLine()) {
                    String line = in.nextLine().trim();
                    index = line.indexOf(" ");
                    while ( index != -1 ) {
                        String word = line.substring(0,line.indexOf(" "));
                        if ( isSentenceEnd(word) ) sentences++;
                        word = cleanUp(word);








                        //find hard words in the word list
                        int DCindex = Collections.binarySearch(DCwords,word);
                        if(DCindex > -1){
                          //System.out.println("found index of "+ word + " at " + DCindex);
                          notHard++;
                        }else{
                          hardWordCount++;
                          //System.out.println("HARD");
                        }








                        Entry wordObject = new Entry(word);
                        int arrayIndex = Collections.binarySearch(words, wordObject, wordComparator);
                        if ( arrayIndex < 0 ) {
                            int insertionPoint = -arrayIndex-1;
                            words.add(insertionPoint, wordObject);
                            words.get(insertionPoint).addLine(lineNumber);
                            words.get(insertionPoint).checkPronoun(word);
                            words.get(insertionPoint).isExcludedWord();
                            words.get(insertionPoint).countSyllables();
                           }
                        else {
                           words.get(arrayIndex).addLine(lineNumber);
                           words.get(arrayIndex).checkPronoun(word);
                           words.get(arrayIndex).isExcludedWord();
                           }



                        line = line.substring(line.indexOf(" ")+1,line.length()).trim();
                        index = line.indexOf(" ");
                    }
                    String word = line.substring(0,line.length());
                    if ( isSentenceEnd(word) ) sentences++;
                    word = cleanUp(word);
                        Entry wordObject = new Entry(word);
                        int arrayIndex = Collections.binarySearch(words, wordObject, wordComparator);
                        if ( arrayIndex < 0 ) {
                            int insertionPoint = -arrayIndex-1;
                            words.add(insertionPoint, wordObject);
                            words.get(insertionPoint).addLine(lineNumber);
                            words.get(insertionPoint).checkPronoun(word);
                            words.get(insertionPoint).isExcludedWord();
                            words.get(insertionPoint).countSyllables();
                           }
                        else {
                           words.get(arrayIndex).addLine(lineNumber);
                           words.get(arrayIndex).checkPronoun(word);
                           words.get(arrayIndex).isExcludedWord();
                           }
                    //System.out.println(word);
                    index = 0;
                    lineNumber++;


                }

                //System.out.println("The size of the ArrayList is " + words.size());
                Collections.sort(words, wordCountComparator);


                System.out.println("*** DOCUMENT ANALYZER ***\n\n");

                int wordCount = 0;
                int syllables = 0;
                for (int i=0; i<words.size(); i++) {
                     wordCount += words.get(i).getWordCount();
                     syllables += words.get(i).getSyllables() * words.get(i).getWordCount();
                     }

                System.out.println(" Words = " + wordCount);
                System.out.println(" Syllables = " + syllables);
                System.out.println(" Sentences = " + sentences);

                double readabilityIndex = 206.835 -
                                          84.6 * (double) syllables / (double) wordCount -
                                          1.015 * (double) wordCount / (double) sentences;

                System.out.println("The Flesch Reading index is " + readabilityIndex);


                double gradeLevel = (0.39 * (double) wordCount / (double) sentences ) +
                                    (11.8 * (double) syllables / (double) wordCount ) - 15.59;


                System.out.println("The Flesch-Kincaid Grade Level is " + gradeLevel);








                //DALE CHALL TEST
                System.out.println("DC hard words:" + hardWordCount + "\nDC not hard words:" + notHard);
                double hardPercent = ((double)hardWordCount/(double)wordCount)*100;
                double daleChall = 0.1579*(hardPercent) + 0.0496*((double)wordCount/(double)sentences);
                if(hardPercent > 5){
                  daleChall += 3.6365;
                }
                System.out.println("Hard word %: " + hardPercent);
                System.out.println("The Dale-Chall readability index is " + daleChall);



            }

        private static String cleanUp( String word ){
            if (word.endsWith("'s")) word = word.substring(0,word.length()-2);
            word = word.replace('\'', ' ').trim();
            word = word.replace('[', ' ').trim();
            word = word.replace(']', ' ').trim();
            word = word.replace('.', ' ').trim();
            word = word.replace(',', ' ').trim();
            word = word.replace(';', ' ').trim();
            word = word.replace(':', ' ').trim();
            word = word.replace('!', ' ').trim();
            word = word.replace('?', ' ').trim();
            word = word.replace('(', ' ').trim();
            word = word.replace(')', ' ').trim();

            return word;
        }

        private static boolean isSentenceEnd(String testWord){
        return    ( (testWord.indexOf('.') > 0)  ||  (testWord.indexOf(':') > 0 )  ||
                    (testWord.indexOf(';') > 0)  ||  (testWord.indexOf('?') > 0 )  ||
                    (testWord.indexOf('!') > 0)   );
        }



    }
