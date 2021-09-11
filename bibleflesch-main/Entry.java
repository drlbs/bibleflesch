import java.util.ArrayList;

class Entry{

    public String word;
    // The lines array list keeps up with which lines in the input file the word occurred on
    public ArrayList<Integer> lines = new ArrayList<Integer>();
    boolean pronoun = true; 
    boolean isNumber = false;
    boolean excludedWord = false;
    boolean connectingWord = false;
    int syllables = 0;

    public Entry () {
       word = "";
       }

    public Entry ( String newWord ) {
       word = newWord;
       checkIfNumber();
       }

    public void addLine ( int line ){
       lines.add(line);
       }

    public int getWordCount(){
       return lines.size(); 
       }

    public void checkPronoun ( String newWord ){
       if (word.toLowerCase().equals(newWord)) pronoun = false;  
       if ((lines.size() == 1) && (word.toLowerCase().equals(word))) pronoun = false;
       }

    public boolean getPronoun(){
       return pronoun; 
       }

    public boolean getIsNumber(){
       return isNumber; 
       }

    public void printArray(){
       for (int i=0; i<lines.size(); i++ )
           System.out.println("                " + lines.get(i));
       }

    public boolean includeInList(){
       return !excludedWord;
       }

    private void checkIfNumber(){
       isNumber = !( (word.indexOf('0') < 0)  &&  (word.indexOf('1') < 0 )  &&  
                     (word.indexOf('2') < 0)  &&  (word.indexOf('3') < 0 )  && 
                     (word.indexOf('4') < 0)  &&  (word.indexOf('5') < 0 )  && 
                     (word.indexOf('6') < 0)  &&  (word.indexOf('7') < 0 )  && 
                     (word.indexOf('8') < 0)  &&  (word.indexOf('9') < 0 ) );
       }

    public void isExcludedWord(){
       connectingWord =  word.equalsIgnoreCase("and") ||
                         word.equalsIgnoreCase("but") ||
                         word.equalsIgnoreCase("or")  ||
                         word.equalsIgnoreCase("the") ||
                         word.equalsIgnoreCase("an") ||
                         word.equalsIgnoreCase("a") ||
                         word.equalsIgnoreCase("chapter"); 
       boolean poundSign =  word.equalsIgnoreCase("#");
       excludedWord = connectingWord || isNumber || pronoun || poundSign;
       }

    public boolean isConnectingWord(){
       return connectingWord;  
       }

    public double getW(int totalLinesInFile ){
       double numerator = 0.0;
       for (int i=0; i<lines.size(); i++) 
           numerator += lines.get(i);
       numerator = numerator / (double) lines.size();
       return (double) numerator / (double) totalLinesInFile * 100.0;
       } 
  
    public void countSyllables(){
      syllables = 0;
      int[] vowelBits = new int[word.length()];
      if ( word.length() != 0 ) { 
       
      for (int i=0;i<word.length();i++)  
          if (isVowel(word.substring(i,i+1))) vowelBits[i] = 1;

      if (word.length() > 1 )
      for (int i=0;i<word.length()-1;i++)  
          if (( vowelBits[i] != 0 ) && ( vowelBits[i+1] != 0 ))  vowelBits[i+1] = 2*vowelBits[i];


      if ( word.substring(word.length()-1).equalsIgnoreCase("e") ) vowelBits[word.length()-1] = 0;

      for (int i=0;i<word.length();i++) 
          if  (vowelBits[i] == 1) syllables++;
  
      if (syllables == 0) syllables++;
      }
      else syllables = 0;

      for (int x : vowelBits) System.out.print(x); System.out.println(" " + this.word + " has " + syllables + " syllables.");
      } 
       
    public int getSyllables(){
       return syllables;
      }                  
   
    private boolean isVowel(String check){
        String[] vowels = {"a", "e", "i", "o", "u", "y"};
        boolean isAVowel = false;
        for ( String v : vowels) 
            if ( check.equalsIgnoreCase(v) ) isAVowel = true;
        return isAVowel;
        }
     
             
}

