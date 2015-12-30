import java.io.IOException;
import java.io.InputStream;

/**
 * LWZ Decompression algorithm for converting compressed data back to text
 *
 * @author Nikhil Verma
 * @version 1.0: StringZipInputStream.java
 *          Revisions:
 *          Initial revision
 */
public class StringZipInputStream	{

    private InputStream inputStream;
    private int codeWidth=12;
    private StringCode[] stringCodes;
    private int count;
    private int codeRange;
    private int outstandingBitCounter;
    private boolean outstandingBitsLeft=false;
    private byte outstandingLowBits;
    private int oldCode;
    private boolean firstGo=true;

    // Creates a new input stream with a default buffer size.
    public StringZipInputStream(InputStream out)	{
        inputStream=out;
        codeRange= (int) Math.pow(2, codeWidth);
        stringCodes=new StringCode[codeRange];
//        Dictionary.prePopulatedDictionary(symbolTableSize);
        count=0;
        for (char ch = 0; ch < 2*Byte.MAX_VALUE; ch++) {
            String symbol=ch+"";
            addToTable(symbol);
        }
    }

    private boolean addToTable(String symbol) {
        if(count>=codeRange){
            return false;
        }
        if(count>=352){
//            System.out.println("symbol to add "+symbol);
            int a=1;
        }
        stringCodes[count]=new StringCode(symbol,count);
        count++;
        return true;
    }

    public void printByteArray(byte[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+",");
        }
    }

    /**
     *Reads data into a string. the method will block until some input can be read; otherwise, no bytes are read and null is returned.
     */
    public String read() throws IOException {
        int bufferSize = (int) (((float)codeWidth / 8)*100);//read 100 codes at a time
        byte[]bytes=new byte[bufferSize];
        int numberOfBytesRead = inputStream.read(bytes);

        if (numberOfBytesRead!=-1) {
            StringBuilder stringBuilder=new StringBuilder(500);//constant amortized
            InputBuffer inputBuffer=new InputBuffer(codeWidth,bytes,numberOfBytesRead);
            if(outstandingBitsLeft){
                inputBuffer.init(outstandingLowBits,outstandingBitCounter);
                outstandingBitsLeft=false;
                System.out.println("Using existing bytes");
            }

//            int oldCode;
            if (firstGo) {//TODO experimental
                oldCode = inputBuffer.read();
                firstGo=false;
                stringBuilder.append(stringCodes[oldCode].symbol);
            }
            char character= stringCodes[oldCode].symbol.charAt(0);
            while(inputBuffer.hasNext()){
                int newCode=inputBuffer.read();
                if(newCode==-1){
                    outstandingBitsLeft=true;
                    outstandingBitCounter=inputBuffer.getBitCounter();
                    outstandingLowBits=inputBuffer.getBitsLeftFromLastByte();
                    System.out.println("last few bits left");
                    break;
                }
                String string;
                if(stringCodes[newCode]==null){//empty
                    string=stringCodes[oldCode].symbol;
                    string=string+character;
                }else{
                    string = stringCodes[newCode].symbol;
                }
                stringBuilder.append(string);

                character = string.charAt(0);
                String combination=stringCodes[oldCode].symbol+character;
                addToTable(combination);
                oldCode=newCode;
            }
            return stringBuilder.toString();
        }else{
            return null;
        }
    }
    // Closes this input stream and releases any system resources associated with the stream.
    public void close() throws IOException {
        inputStream.close();
    }
} 