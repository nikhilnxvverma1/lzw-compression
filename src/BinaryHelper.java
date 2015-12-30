/**
 * Created by NikhilVerma on 12/10/15.
 */
public class BinaryHelper {
    private static final int BYTE_SIZE=8;

    public static int getNumber(String bits){
        int number=0;
        int power= bits.length();
        while(power-1>=0){
            if(bits.charAt(bits.length()-power)=='1'){
                number+=Math.pow(2,power-1);
            }
            power--;
        }
        return number;
    }

    public static String getBits(int n){
        return Integer.toBinaryString(n);
    }

    public static String getLowBits(String bits, int low){
        if(low>bits.length()){
            return bits;
        }
        return bits.substring(bits.length()-low,bits.length());
    }

    public static String getHighBits(String bits, int high){
        if(high>bits.length()){
            return bits;
        }
        return bits.substring(0,high);
    }

    public static String padToLength(String bits,int length){
        if(length<bits.length()){
            return getLowBits(bits,length);
        }else{
            int zerosToPad=length-bits.length();
            StringBuilder paddedWithZeros=new StringBuilder();
            for (int i = 0; i < zerosToPad; i++) {
                paddedWithZeros.append('0');
            }
            paddedWithZeros.append(bits);
            return paddedWithZeros.toString();
        }
    }

    public static String getLowBits(byte b,int low){

        String bits = padToLength(getBits(b), BYTE_SIZE);
        StringBuilder lowBits=new StringBuilder();
        for (int i = bits.length()-low; i <bits.length(); i++) {
            lowBits.append(bits.charAt(i));
        }
        return lowBits.toString();


//        if(low>BYTE_SIZE){
//            return Integer.toBinaryString(b);
//        }
//        byte ander=0b00000000;
//        switch (low){
//            case 1:
//                ander=0b00000001;
//                break;
//            case 2:
//                ander=0b00000011;
//                break;
//            case 3:
//                ander=0b00000111;
//                break;
//            case 4:
//                ander=0b00001111;
//                break;
//            case 5:
//                ander=0b00011111;
//                break;
//            case 6:
//                ander=0b00111111;
//                break;
//            case 7:
//                ander=0b01111111;
//                break;
//            case 8:
//                ander=0b1111111;
//                break;
//
//        }
//        byte lowBits= (byte) (b&ander);
//        return Integer.toBinaryString(lowBits);
    }

    public static String getHighBits(byte b,int high){

        String bits = padToLength(getBits(b), BYTE_SIZE);
        StringBuilder highBits=new StringBuilder();
        for (int i = 0; i < high; i++) {
            highBits.append(bits.charAt(i));
        }
        return highBits.toString();

//        if(high>BYTE_SIZE){
//            return Integer.toBinaryString(b);
//        }
//        short ander=0b00000000;
//        switch (high){
//            case 1:
//                ander=0b10000000;
//                break;
//            case 2:
//                ander=0b11000000;
//                break;
//            case 3:
//                ander=0b11100000;
//                break;
//            case 4:
//                ander=0b11110000;
//                break;
//            case 5:
//                ander=0b11111000;
//                break;
//            case 6:
//                ander=0b11111100;
//                break;
//            case 7:
//                ander=0b11111110;
//                break;
//            case 8:
//                ander=0b11111111;
//                break;
//
//        }
//        byte highBits= (byte) (b&ander);
//        return Integer.toBinaryString(highBits);
    }
}
