package cpu.alu;

import util.Transformer;
import util.DataType;
import java.util.Arrays;

public class ALU {
    public String add(String src, String dest){
        return adder(src,dest,'0',32);
    }

    public String sub(String src, String dest){

        return adder(src,negation(dest),'1',32);
    }

    public String mul(String src,String dest){
        src=supplement(src,32);
        dest=supplement(dest,32);
        String res = get_allzeros(32);
        String negSrc = adder(res,negation(src),'1',32);
        String product = res+dest;
        int len=dest.length();
        int Y0=0;
        int Y1=product.charAt(2*len-1);
        for(int i=0;i<len;++i){
            switch (Y0-Y1){
                case 1:
                    product=adder(product.substring(0,len),src,'0',32)+product.substring(len);
                    break;
                case -1:
                    product=adder(product.substring(0,len),negSrc,'0',32)+product.substring(len);
                    break;
            }
            product=product.substring(0,1)+product.substring(0,2*len-1);
            Y0=Y1;
            Y1=product.charAt(2*len-1);
        }
        return product.substring(len);
    }

    public String div(String src,String dest){
        //src是被除数,dest是除数
        src=supplement(src,32);
        dest=supplement(dest,32);
        String zero=get_allzeros(32);
        int len=src.length();
        if(src.equals(zero)&&!dest.equals(zero)){
            return zero;
        }else if(!src.equals(zero)&&dest.equals(zero)){
            return "除数为零错误";
        }else if(src.equals(zero)&&dest.equals(zero)){
            return "除法错";
        }else{
            String product=supplement(src,64);
            String remainder=product.substring(0,len);
            String quotient=product.substring(len);
            for(int i=0;i<len;++i){
                remainder=remainder.substring(1,len)+quotient.charAt(0);
                char bef=remainder.charAt(0);
                if(remainder.charAt(0)==dest.charAt(0)){
                    remainder=sub(remainder,dest);
                    if(bef!=remainder.charAt(0)){
                        remainder=add(remainder,dest);
                        quotient=quotient.substring(1,len)+"0";
                    }else{
                        quotient=quotient.substring(1,len)+"1";
                    }
                }else{
                    remainder=add(remainder,dest);
                    if(bef!=remainder.charAt(0)){
                        remainder=sub(remainder,dest);
                        quotient=quotient.substring(1,len)+"0";
                    }else{
                        quotient=quotient.substring(1,len)+"1";
                    }
                }
                product=remainder+quotient;
                //System.out.println(product);
            }
            if(src.charAt(0)!=dest.charAt(0)){
                return adder(zero,negation(product.substring(len)),'1',32);
            }else{
                return product.substring(len);
            }
        }
    }

    public String get_allzeros(int len){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<len;++i){
            stringBuilder.append("0");
        }
        return stringBuilder.toString();
    }

    public static String supplement(String op,int length){
        int len=length-op.length();
        StringBuilder stringBuilder = new StringBuilder(op);
        stringBuilder=stringBuilder.reverse();
        for(int i=0;i<len;++i){
            stringBuilder.append(op.charAt(0));
        }
        return stringBuilder.reverse().toString();
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

    public static String full_adder(char x,char y,char c){
        int bit=(x-'0')^(y-'0')^(c-'0');
        int carry=((x-'0')&(y-'0'))|((x-'0')&(c-'0'))|((y-'0')&(c-'0'));
        return ""+carry+bit;
    }

//    private static String fullAdder(char x, char y, char c) {
//        int bit = (x - '0') ^ (y - '0') ^ (c - '0');  //三位异或
//        int carry = ((x - '0') & (y - '0')) | ((y - '0') & (c - '0')) | ((x - '0') & (c - '0'));  //有两位为1则产生进位
//        return "" + carry + bit;  //第一个空串让后面的加法都变为字符串加法
//    }

    public static String adder(String op1,String op2,char carry,int length){
        op1=supplement(op1,length);
        //System.out.println(op1);
        op2=supplement(op2,length);
        StringBuilder stringBuilder=new StringBuilder();
//        for(int i=0;i<length;++i){
//            System.out.println(op1.charAt(i));
//        }
        //System.out.println(op2);
        //String ans="";
        char c=carry;
        for(int i=length-1;i>=0;--i){
            String res=full_adder(op1.charAt(i),op2.charAt(i),c);
            //System.out.println(res);
            c=res.charAt(0);
            //char bit=res.charAt(1);
            //ans = res.charAt(1)+ans;
            stringBuilder.append(res.charAt(1));
        }
        //System.out.println(ans);
        return stringBuilder.reverse().toString();
    }

    String serialCarryAdder(String adder1, String adder2) {//串行进位加法器
        StringBuilder result = new StringBuilder();
        char carry = '0';
        String output = "00";
        for (int i = adder1.length() - 1; i >= 0; i--) {
            carry = output.charAt(0);
            output = full_adder(adder1.charAt(i), adder2.charAt(i), carry);
            result.append(output.charAt(1));
        }
        // 溢出、进位判断
        //CF = String.valueOf(output.charAt(1));
        //OF = String.valueOf((carry - '0') ^ (output.charAt(1)));//Cn和Cn-1取异或

        return result.reverse().toString();
    }

    String carryLookAheadAdder_8(String adder1, String adder2, char carryIn) {//全先行进位加法器
        char[] xChars = adder1.toCharArray();
        char[] yChars = adder2.toCharArray();
        int[] x = new int[8];
        int[] y = new int[8];

        for (int i = 7; i >= 0; i--) {
            x[i] = xChars[7 - i] - '0';
            y[i] = yChars[7 - i] - '0';
        }

        //1个时间单位
        int[] p = new int[8];
        int[] g = new int[8];
        for (int i = 0; i < 8; i++) {
            p[i] = x[i] | y[i];
            g[i] = x[i] & y[i];
        }

        //两个时间单位
        int[] c = new int[8];//每一位的carryOut
        for (int i = 0; i < 8; i++) {
            c[i] = g[i];
            int temp = 1;
            for (int j = i; j >= 1; j--) {
                temp = temp & p[j];
                c[i] = c[i] | (temp & g[j - 1]);
            }
            c[i] = c[i] | (temp & p[0] & carryIn);
        }

        //前三个时间单位里同时进行的异或运算
        int[] xorForXY = new int[8];
        for (int i = 0; i < 8; i++) {
            xorForXY[i] = x[i] ^ y[i];
        }

        //第4到第6时间单位
        StringBuilder result = new StringBuilder();
        result.append(xorForXY[0] ^ (carryIn - '0'));
        for (int i = 1; i < 8; i++) {
            result.append(xorForXY[i] ^ c[i - 1]);
        }
        result.reverse();
        result.append(c[7]);//最后的carryOut
        return result.toString();
    }

    String connectedCLAAdder(String adder1, String adder2) {//部分先行进位加法器
        String result = "";

        String CLAresult = carryLookAheadAdder_8(adder1.substring(24, 32), adder2.substring(24, 32), '0');
        result = CLAresult.substring(0, 8) + result;

        CLAresult = carryLookAheadAdder_8(adder1.substring(16, 24), adder2.substring(16, 24), CLAresult.charAt(8));
        result = CLAresult.substring(0, 8) + result;

        CLAresult = carryLookAheadAdder_8(adder1.substring(8, 16), adder2.substring(8, 16), CLAresult.charAt(8));
        result = CLAresult.substring(0, 8) + result;

        CLAresult = carryLookAheadAdder_8(adder1.substring(0, 8), adder2.substring(0, 8), CLAresult.charAt(8));
        result = CLAresult.substring(0, 8) + result;

        //CF = String.valueOf(CLAresult.charAt(8));
        int x = adder1.charAt(0) - '0';
        int y = adder2.charAt(0) - '0';
        int s = result.charAt(0) - '0';
        //OF = (String.valueOf((x & y & ~s) | (~x & ~y & s)));//不好找Cn-1，所以计算繁琐一点
        return result;

    }


}
