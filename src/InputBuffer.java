/**
 * Created by NikhilVerma on 12/10/15.
 */
public class InputBuffer {
    private static final int BYTE_SIZE=8;
    private int codeWidth;
    private int byteCounter=0;
    private int bitCounter=0;
    private byte[] buffer;
    private int totalBytesRead;


    public InputBuffer(int codeWidth,byte []buffer,int totalBytesRead) {
        this.buffer=buffer;
        this.codeWidth = codeWidth;
        this.totalBytesRead = totalBytesRead;
    }

    public void init(byte b,int bitCounter){
        //prepend this to existing to byte
        byte[] newBuffer=new byte[buffer.length+1];
        newBuffer[0]=b;
        for (int i = 0; i < buffer.length; i++) {
            newBuffer[i+1]=buffer[i];
        }
        buffer=newBuffer;
        this.bitCounter=bitCounter;
    }

    public int getCodeWidth() {
        return codeWidth;
    }

    public int read(){
        int bitsLeft=BYTE_SIZE-bitCounter+(buffer.length-byteCounter-1)* codeWidth;
        if(bitsLeft< codeWidth){
//        if (bitsLeft < BYTE_SIZE) {//TODO experimental
            byteCounter++;//increment so as to exhaust off the buffer pointer
            return -1;
        }
        //read off 'codeWidth' bits from the buffer
        StringBuilder bitsBuilder=new StringBuilder();

        int bitsReadSoFar=0;
        boolean readAsHighBits=false;
        while(bitsReadSoFar< codeWidth){
            byte b=buffer[byteCounter];

            //get low bits from the current byte
            //low bits that are left from the current byte

            String bitsRead;
            int bitsToRead;
            if(readAsHighBits){
                if((codeWidth -bitsReadSoFar)<BYTE_SIZE){
                    bitsToRead = (codeWidth -bitsReadSoFar);
                }else{
                    bitsToRead=BYTE_SIZE;
                }
                bitsRead=BinaryHelper.getHighBits(b,bitsToRead);
            }else{
                bitsToRead=BYTE_SIZE-bitCounter;
                bitsRead=BinaryHelper.getLowBits(b, bitsToRead);
            }
            bitsBuilder.append(bitsRead);

            if(bitCounter+bitsToRead>=BYTE_SIZE){
                bitCounter=(bitCounter+bitsToRead)%BYTE_SIZE;
                assert bitCounter==0;
                byteCounter++;
                readAsHighBits=true;
            }else{
                bitCounter+=bitsRead.length();
            }

            bitsReadSoFar+=bitsToRead;
        }

        return BinaryHelper.getNumber(bitsBuilder.toString());
    }

    public boolean hasNext(){
        return byteCounter< totalBytesRead &&(byteCounter-1)<buffer.length;
    }

    public byte getBitsLeftFromLastByte(){
        int lowBitsLeft=BYTE_SIZE-bitCounter;
        String bits = BinaryHelper.padToLength(BinaryHelper.getBits(buffer[byteCounter-1]), 8);//-1 cause it just incremented
        String lowBits = BinaryHelper.getLowBits(bits, lowBitsLeft);
        return (byte) BinaryHelper.getNumber(lowBits);
    }

    public int getBitCounter() {
        return bitCounter;
    }
}
