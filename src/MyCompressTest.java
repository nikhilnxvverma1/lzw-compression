

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
// import java.util.zip.StringZipInputStream;
// import java.util.zip.StringZipOutputStream;

public class MyCompressTest	{
    final  int MAX = 1024;
    String inputFileName 	= "resources/words.txt";//mind the location
    String outputFileName 	= "words.compress";
    String uncompressed 	= "words.uncompress";

    void compress()	{
        try {
            String aWord;

            BufferedReader input = new BufferedReader(new FileReader(inputFileName));
            StringZipOutputStream aStringZipOutputStream = new StringZipOutputStream( new FileOutputStream(outputFileName));

            while (  ( aWord = input.readLine() )  != null ) {
                aStringZipOutputStream.write(aWord);
            }
            aStringZipOutputStream.close();
            input.close();

        } catch ( Exception e )	{
            e.printStackTrace();
            System.exit(1);
        }
    }
    void unCompress()	{
        try {
            String aWord;
            byte[] buffer = new byte[MAX];

            BufferedWriter uncompress = new BufferedWriter(new FileWriter(uncompressed));
            StringZipInputStream aStringZipInputStream = new StringZipInputStream( new FileInputStream(outputFileName));//change here
            String theWord;

            while (  ( theWord = aStringZipInputStream.read() ) != null ) {
//                System.out.println(theWord);
                uncompress.write(theWord, 0, theWord.length());
            }
            aStringZipInputStream.close();
            uncompress.close();

        } catch ( Exception e )	{
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void main( String args[] ) {
        MyCompressTest aMyCompressTest = new MyCompressTest();
        System.out.println("Starting Compression");
        aMyCompressTest.compress();
        System.out.println("Compression done!");
        System.out.println("Decompressing now");
        aMyCompressTest.unCompress();
        System.out.println("Decompression done!");
    }
}