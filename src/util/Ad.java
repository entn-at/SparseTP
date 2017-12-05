package util;

import java.util.ArrayList;

/**
 * Created by huangwaleking on 9/11/17.
 */
public class Ad {

    //f_[i][x]=f_i(x)
    public static ArrayList<ArrayList<Tuple>>[][] f_;

    public static ArrayList<ArrayList<Tuple>>[][] F_;

    /*
     * two initial condition:  (1) F_0(x)=f_0(x); (2) F_i(0)={[null]}
     */
    public static void init_F(){
        initAccumulatePhraseSize();
        //Step 0, init F_[][] array
        F_=(ArrayList<ArrayList<Tuple>>[][])(new ArrayList[listPhraseSize.length][]);
        for(int i=0;i<listPhraseSize.length;i++){
            F_[i]=(ArrayList<ArrayList<Tuple>>[])(new ArrayList[accumulatePhraseSize[i]+1]);
        }
        //Step 1, init by the initial condition F_0(x)=f_0(x)
        for(int x=0;x<=listPhraseSize[0];x++){
            F_[0][x]=f_[0][x];
        }
        //Step 2, init by the initial condition F_i(0)={[null]}
        for(int i=0;i<accumulatePhraseSize.length;i++){
            F_[i][0]=new ArrayList<ArrayList<Tuple>>();
            F_[i][0].add(new ArrayList<Tuple>());
        }
    }

    public static void init_f(int[] listPhraseSize){
        f_=(ArrayList<ArrayList<Tuple>>[][])(new ArrayList[listPhraseSize.length][]);
        for(int i=0;i<listPhraseSize.length;i++){
            f_[i]=(ArrayList<ArrayList<Tuple>>[])(new ArrayList[listPhraseSize[i]+1]);
        }
        for(int i=0;i<listPhraseSize.length;i++){
            for(int x=0;x<=listPhraseSize[i];x++){
                if(x==0){
                    f_[i][x]=new ArrayList<ArrayList<Tuple>>();
                    f_[i][x].add(new ArrayList<Tuple>());
                }else{
                    ArrayList<Tuple> l=new ArrayList<>();
                    l.add(new Tuple(i,x));
                    f_[i][x]=new ArrayList<ArrayList<Tuple>>();
                    f_[i][x].add(l);
                }
            }
        }
    }

    public static ArrayList<ArrayList<Tuple>> get_F_(int i, int x){
        if(x<=accumulatePhraseSize[i]){
            return F_[i][x];
        }else {
            ArrayList<ArrayList<Tuple>> s = new ArrayList<ArrayList<Tuple>>();
            s.add(new ArrayList<Tuple>());
            return s;
        }
    }

    public static ArrayList<ArrayList<Tuple>> get_f_(int i, int x){
        if(x<=listPhraseSize[i]){
            return f_[i][x];
        }else {
            ArrayList<ArrayList<Tuple>> s = new ArrayList<ArrayList<Tuple>>();
            s.add(new ArrayList<Tuple>());
            return s;
        }
    }

    public static void update_F(){
        for(int i=1;i<accumulatePhraseSize.length;i++){
            for(int x=1;x<accumulatePhraseSize[i]+1;x++){
                //update F_[i][x]=\cup_{b=0}^{x} (f_[i][b] \oplus F_[i-1][x-b])
                System.out.println("updating F_["+i+"]["+x+"]. ");
                ArrayList<ArrayList<Tuple>> result=new ArrayList<ArrayList<Tuple>>();
                for(int b=0;b<=x;b++){
                    if(b<=listPhraseSize[i] && (x-b)<=accumulatePhraseSize[i-1]){
                        oplus(result,get_f_(i,b),get_F_(i-1,x-b));
                    }
                }
                F_[i][x]=result;
//                System.out.println("after updating, F_["+i+"]["+x+"]="+outputSet(get_F_(i,x)));
            }
        }
    }

    /**
     * outputSet call outputList, which call Tuple.toString()
     * @param set
     * @return
     */
    public static String outputSet(ArrayList<ArrayList<Tuple>> set){
        StringBuffer sb=new StringBuffer();
        if(set!=null){
            sb.append("{");
            for (ArrayList<Tuple> l : set) {
                sb.append(outputList(l)+",");
            }
            sb.append("}");
        }else{
            sb.append("null");
        }
        return sb.toString();
    }

    public static String outputList(ArrayList<Tuple> list){
        StringBuffer sb=new StringBuffer();
        sb.append("[");
        if(list.size()>0){
            for(Tuple t: list){
                sb.append(t.toString()+" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String output_f_(){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<listPhraseSize.length;i++){
            for(int x=0;x<=listPhraseSize[i];x++){
                sb.append(outputSet(get_f_(i,x)));
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String output_F_(){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<accumulatePhraseSize.length;i++){
            for(int x=0;x<=accumulatePhraseSize[i];x++){
                sb.append(outputSet(get_F_(i,x)));
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    public static int[] listPhraseSize;

    public static int[] accumulatePhraseSize;//listPhraseSize=[2,2,2], then accumulatePhraseSize=[2,4,6]

    public static void initAccumulatePhraseSize(){
        accumulatePhraseSize=new int[listPhraseSize.length];
        accumulatePhraseSize[0]= listPhraseSize[0];
        for(int i=1;i<listPhraseSize.length;i++){
            accumulatePhraseSize[i]= accumulatePhraseSize[i-1]+listPhraseSize[i];
        }
    }

    public static ArrayList<Tuple> combineArrayList(ArrayList<Tuple> u, ArrayList<Tuple> v){
        ArrayList<Tuple> result=new ArrayList<Tuple>();
        if(u.size()>0){
            if(u.size()>100){
                System.out.println("u.size="+u.size());
            }
            for (int i=0;i<u.size();i++) {
                result.add(u.get(i));
            }
        }
        if(v.size()>0){
            if(v.size()>100){
                System.out.println("v.size="+v.size());
            }
            for(int i=0;i<v.size();i++){
                result.add(v.get(i));
            }
        }
//        if(u.size()>0) {
//            for (Tuple t : u) {
//                result.add(t);
//            }
//        }
//        if(v.size()>0){
//            for(Tuple t: v){
//                result.add(t);
//            }
//        }
        return result;
    }

    /**
     * set1 oplus set2= {u+v}, u \in set1, v \in set2
     * then update result=set1 oplus set2
     * @param result
     * @param set1
     * @param set2
     */
    public static void oplus(ArrayList<ArrayList<Tuple>> result, ArrayList<ArrayList<Tuple>> set1, ArrayList<ArrayList<Tuple>> set2){
        for(ArrayList<Tuple> u: set1){
            for(ArrayList<Tuple> v: set2){
                result.add(combineArrayList(u,v));
            }
        }
    }

    public static void init_listPhraseSize(int[] _listPhraseSize){
        listPhraseSize=_listPhraseSize;
    }


    public static double getAd(int[] _listPhraseSize,int K){
        init_listPhraseSize(_listPhraseSize);
        init_f(listPhraseSize);
        init_F();
        update_F();
        double Ad= AdDPResult.getFinalResult(K,listPhraseSize,F_);
        System.out.println("Ad by Dynamic Programming="+Ad);
        return Ad;
    }

    public static void main(String[] args){
        getAd(new int[]{2,2,3,3},100);

//        int[] _listPhraseSize=new int[]{2,3,3};
//        init_listPhraseSize(_listPhraseSize);
//        init_f(listPhraseSize);
////        System.out.println(output_f_());
//        init_F();
////        System.out.println(output_F_());
//        update_F();
////        System.out.println(output_F_());
//
//        int K=2;
//        double groundTruth=AdGroundTruth.getGroundTruth(K,listPhraseSize);
//        System.out.println("Ground Truth="+groundTruth);
//        double Ad= AdDPResult.getFinalResult(K,listPhraseSize,F_);
//        System.out.println("Ad by Dynamic Programming="+Ad);
    }
}
