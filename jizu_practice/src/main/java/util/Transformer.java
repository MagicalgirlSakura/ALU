package util;

import java.util.Collections;

public class Transformer {
    public static  String intToBinary(String numStr){
        boolean isNeg = false;
        int num = Integer.parseInt(numStr);
        if(num==0) return Collections.nCopies(32,"0").toString();
        if(num==0x80000000) return "1"+Collections.nCopies(31,"0").toString();
        if(num<0){
            num=-num;
            isNeg=true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while(num>0){
            stringBuilder.append(""+num%2);
            num/=2;
        }
        int len = stringBuilder.length();
        for(int i=0;i<32-len;i++){
            stringBuilder.append("0");
        }
        if(!isNeg){
            return stringBuilder.reverse().toString();
        }else{
            return oneAdder(negation(stringBuilder.reverse().toString())).substring(1);
        }
    }

    public static String negation(String s){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<32;i++){
             if(s.charAt(i)=='0'){
                 stringBuilder.append("1");
             }else{
                 stringBuilder.append("0");
             }
         }
        return stringBuilder.toString();
    }

    public static String oneAdder(String s){
        int len = s.length();
        StringBuilder stringBuilder = new StringBuilder(s);
        int[] nums = new int[len];
        stringBuilder=stringBuilder.reverse();
        for(int i=0;i<32;i++){
            nums[i]=stringBuilder.charAt(i)-'0';
        }
        int bit =0x0;
        int carry =0x1;
        char[] chars = new char[len];
        for(int i=0;i<32;i++){
            bit=bit^carry;
            carry=bit&carry;
            chars[i]=(char)bit;
        }
        String res = new StringBuilder(new String(chars)).reverse().toString();
        return ""+(res.charAt(0)==s.charAt(0)?"0":"1")+res;
    }

    public static String binaryToInt(String s){
        int ans=0;
        int temp=0;
        int len = s.length();
        for(int i=0;i<len;++i){
            temp=s.charAt(i)-'0';
            ans=ans*2+temp;
        }
        return String.valueOf(ans);
    }

    public static String decimalToNBCD(String decimal){
        int num = Integer.parseInt(decimal);
        String sign="";
        String res="";
        sign=num<0?"1101":"1100";
        num=Math.abs(num);
        for(int i=0;i<7;++i){
             int tempNum=num%10;
             res=getNBCD_4(tempNum).concat(res);
             num=num/10;
        }
        return sign.concat(res);

    }

    public static String getNBCD_4(int num){
        String res="";
        for(int i=0;i<4;++i,num/=2){
            if(num%2==0){
                res="0".concat(res);
            }else{
                res="1".concat(res);
            }
        }
        return res;
    }

    public static String NBCDToDecimal(String NBCDStr){
        StringBuilder stringBuilder = new StringBuilder();
        if(NBCDStr.startsWith("1101")) stringBuilder.append("-");
        NBCDStr = NBCDStr.substring(4);
        for(int i=0;i<NBCDStr.length()&&i<28;i=i+4){
            stringBuilder.append(binaryToInt(NBCDStr.substring(i,i+4)));
        }
        return String.valueOf(Integer.parseInt(stringBuilder.toString()));
    }


}
