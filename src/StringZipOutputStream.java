import java.io.IOException;
import java.io.OutputStream;

/**
 * LZW algorithm for compression of textual data into binary format
 *
 * @author Nikhil Verma
 * @version 1.0: StringZipOutputStream.java
 *          Revisions:
 *          Initial revision
 */
public class StringZipOutputStream	{

    private OutputStream outputStream;
    private int codeWidth = 12;
    private Dictionary dictionary;
    private byte outstandingByte;
    private int outstandingBitCounter=0;
    private String remaining=null;
    // Creates a new output stream with a default buffer size.
    public StringZipOutputStream(OutputStream out)	{
        outputStream=out;
        dictionary=Dictionary.prePopulatedDictionary((int) Math.pow(2,codeWidth));
    }

    /**
     *Writes aStrign compressed output stream. This method will block until all data is written.
     */
    public void write(String aString) throws IOException {

//        String completeString = aString + System.lineSeparator();
        String completeString = aString + "\n";
        int bufferSize = (int) (((float) codeWidth / 8) * completeString.length());
        OutputBuffer outputBuffer=new OutputBuffer(codeWidth , bufferSize+2);
        if(outstandingBitCounter!=0){
            outputBuffer.initWith(outstandingByte,outstandingBitCounter);
        }
        byte[] bytes = completeString.getBytes();
        String string;
        if(remaining!=null){
            string = remaining+(char) (bytes[0]&0xff);
            dictionary.addShorthand(string);
        }
        string = ""+(char) (bytes[0]&0xff);
        for (int i = 1; i < bytes.length; i++) {
            char ch= (char) (bytes[i]&0xff);

            StringCode exists = dictionary.lookup(string + ch);
            if (exists != null) {
                string=string+ch;
            }else{
                StringCode previousStringExists = dictionary.lookup(string);
                outputBuffer.writeCode(previousStringExists.code);
                dictionary.addShorthand(string+ch);
                string=ch+"";
            }
//            outputBuffer.writeCode(bytes[i]);
        }

        StringCode lastSymbol=dictionary.lookup(string);
        outputBuffer.writeCode(lastSymbol.code);

        remaining=string;

        byte[] buffer = outputBuffer.trimAndReturnBuffer();
        outputStream.write(buffer);

        outstandingByte=outputBuffer.getLastByte();
        outstandingBitCounter=outputBuffer.getBitCounter();
    }

    public void printByteArray(byte[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+",");
        }
    }

    // Writes remaining data to the output stream and closes the underlying stream.
    public void close() throws IOException {
//        int bufferSize = (int) (((float) codeWidth / 8) * remaining.length());
//        OutputBuffer outputBuffer=new OutputBuffer(codeWidth , bufferSize);
//        if(outstandingBitCounter!=0){
//            outputBuffer.initWith(outstandingByte,outstandingBitCounter);
//        }
//        StringCode lastSymbol=dictionary.lookup(remaining);
//        outputBuffer.writeCode(lastSymbol.code);
//        byte[] buffer = outputBuffer.trimAndReturnBuffer();
//        outputStream.write(buffer);
        outputStream.flush();
        outputStream.close();
//        System.out.println("Compression done!");
    }
} 