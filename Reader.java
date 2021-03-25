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

                /* We are going to store the words and all of their associated data
                 * in an arraylist of objects of type Entry
                 */
                ArrayList<Entry> words = new ArrayList<Entry>();

                /* These comparators are used to sort the arraylists */

                // This one compares the worlds alphabetically
                Comparator<Entry> wordComparator = new Comparator<Entry>() {
                    public int compare(Entry o1, Entry o2) {
                        return o1.word.compareToIgnoreCase(o2.word);
                    }
                };

                // This compares on the number of time the word occurs
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
                FileReader DCreader = new FileReader("data/DCwordlist1995.txt");
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
                // We process ONE LINE at a time and then parse each word from that line
                while (in.hasNextLine()) {
                    String line = in.nextLine().trim();
                    if (line.isEmpty()){
                      //in.nextLine();
                    }
                    else if (line.charAt(0)<'0' || line.charAt(0)>'9'){

                    }

                    else {

                    if (line.charAt(2) >= '0' && line.charAt(2) <= '9'){
                      line = line.substring(3).trim();
                    }

                    else if (line.charAt(1) >= '0' && line.charAt(1) <= '9'){
                      line = line.substring(2).trim();
                    }
                    else {
                      line = line.substring(1).trim();
                    }

                    index = line.indexOf(" ");
                    line = line.replaceAll("--", "  ").trim();
                    //This straight up isn't working for no good reason
                    while ( index != -1 ) {
                        // Grab the next word "token" from the line looking for a space to end the word
                        String word = line.substring(0,line.indexOf(" "));
                        // If we are at the end of a sentence, then increment the number of sentences.
                        if ( isSentenceEnd(word) ) sentences++;
                        // Now, call the static method to clean up the word,
                        // Questions -- are we doing too much here?
                        word = cleanUp(word);



                        Entry wordObject = new Entry(word);
                        /* At this point we will check to see if we have seen this word before
                         * and if we have not we will add it to the word arraylist and call all
                         * of the methods in the words object to set the instance variables in the class.
                         */
                        int arrayIndex = Collections.binarySearch(words, wordObject, wordComparator);
                        if ( arrayIndex < 0 ) {
                            int insertionPoint = -arrayIndex-1;
                            words.add(insertionPoint, wordObject);
                            words.get(insertionPoint).addLine(lineNumber);
                            words.get(insertionPoint).checkPronoun(word);
                            words.get(insertionPoint).isExcludedWord();
                            words.get(insertionPoint).countSyllables();
                           }
                        /* If we have seen the word before, then just update the instance variables in the class
                         * that need to be updated.
                         */
                        else {
                           words.get(arrayIndex).addLine(lineNumber);
                           words.get(arrayIndex).checkPronoun(word);
                           words.get(arrayIndex).isExcludedWord();
                           }

                        line = line.substring(line.indexOf(" ")+1,line.length()).trim();
                        index = line.indexOf(" ");
                    }

                    /*  This last block is to treat the last word on each line -- everything works just like the
                     * block above that was looking for a space to terminate the work.
                     */
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


                    //find hard words in the word list
                    int DCindex = Collections.binarySearch(DCwords,word);
                    if(DCindex > -1){
                    //System.out.println("found index of "+ word + " at " + DCindex);
                    notHard++;
                    }else{
                    hardWordCount++;
                    //System.out.println("HARD");
                    }

                }

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
            word = word.replace('#', ' ').trim();
            word = word.replace('"', ' ').trim();
            word = word.replace('-', ' ').trim();
            return word;
        }

        private static boolean isSentenceEnd(String testWord){
        return    ( (testWord.indexOf('.') > 0)  ||  (testWord.indexOf(':') > 0 )  ||
                    (testWord.indexOf(';') > 0)  ||  (testWord.indexOf('?') > 0 )  ||
                    (testWord.indexOf('!') > 0)   );
        }



    }
