/**
 * Created by NikhilVerma on 12/10/15.
 */
public class OutputBuffer {

    private static final int MAX_BUFFER_SIZE=100;
    private static final int BYTE_SIZE=8;
    private int codeWidth;
    private byte []buffer;
    private int totalBytes;
    private int byteCounter;
    private int bitCounter;

    /**
     * Creates a output buffer with the speified code width
     * @param codeWidth codeWidth>16 can cause problems
     */
    public OutputBuffer(int codeWidth) {
        this(codeWidth,MAX_BUFFER_SIZE);
    }

    public OutputBuffer(int codeWidth, int totalBytes) {
        this.codeWidth = codeWidth;
        this.totalBytes = totalBytes;
        buffer=new byte[totalBytes];
        byteCounter=0;
        bitCounter=0;
    }

    public void initWith(byte b,int bitCounter){
        buffer[0]=b;
        this.bitCounter=bitCounter;
    }

    public int getCodeWidth() {
        return codeWidth;
    }

    /**
     * writes the code in bits into the byte buffer
     * @param code integer code to write
     * @return true if successful, false indicates an overflow (in which case the code was not written)
     */
    public boolean writeCode(int code){
        int bitsLeft=BYTE_SIZE-bitCounter+(totalBytes-byteCounter-1)*codeWidth;
        if(bitsLeft<codeWidth){
            return false;
        }
//        System.out.print(code + " ");
        //ideally the bit length after removing padded zeros should be equal to codeWidth
        String bits=BinaryHelper.padToLength(BinaryHelper.getBits(code), codeWidth);
//        System.out.println(code+":"+bits);
//        System.out.print(bits);
        assert bits.length()==codeWidth;

        boolean shiftAsHighBits=false;
        String remainingBits=bits;
        while(remainingBits!=null&&remainingBits.length()>0){
            //low bits that are left from the current byte
            int lowBitsLeftFromCurrentByte=BYTE_SIZE-bitCounter;

            //append high bits of the bits to write onto the buffer
            String highBits = BinaryHelper.getHighBits(remainingBits, lowBitsLeftFromCurrentByte);
            int decimalForHighBits=BinaryHelper.getNumber(highBits);
            assert decimalForHighBits>=0&&decimalForHighBits<256;
            buffer[byteCounter]|=decimalForHighBits;

            //increment counter state
            if(bitCounter+highBits.length()>=BYTE_SIZE){
                bitCounter=(bitCounter+highBits.length())%BYTE_SIZE;
                assert bitCounter==0;
//                if(byteCounter==11&&buffer.length==12){
//                    System.out.println("Overflowing");
//                }
                byteCounter++;
                shiftAsHighBits=true;
            }else{
                bitCounter+=highBits.length();
            }

            //we are exhausting the current bits to write
            if(lowBitsLeftFromCurrentByte<remainingBits.length()){
                remainingBits=remainingBits.substring(lowBitsLeftFromCurrentByte);
            }else{
                if(shiftAsHighBits){
                    int shift=BYTE_SIZE-remainingBits.length();
                    buffer[byteCounter]= (byte) (buffer[byteCounter]<<shift);
                }
                remainingBits=null;
            }
        }
        return true;
    }

    public byte[] trimAndReturnBuffer(){
        byte [] trimmed=new byte[byteCounter];
        for (int i = 0; i < byteCounter; i++) {
            trimmed[i]=buffer[i];
        }
        return trimmed;
    }

    public int getBitCounter() {
        return bitCounter;
    }

    public int getByteCounter() {
        return byteCounter;
    }
    public byte getLastByte(){
        return buffer[byteCounter];
    }
}
