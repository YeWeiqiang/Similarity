package sample;

public class PearsonCorrelationScore {
    /**
     *
     * @param vector1
     * @param vector2
     * @return 皮尔逊相关度系数
     */

    public static double method(double[] vector1, double[] vector2){
        double molecular1 = 0;    //分子1
        double molecular2 = 0;    //分子2
        double molecular3 = 0;    //分子3
        double denominator1 = 0;  //分母1
        double denominator2 = 0;  //分母2

        int N = vector1.length;
        for (int i = 0; i < N; i++){
            molecular1 += vector1[i] * vector2[i];
            molecular2 += vector1[i];
            molecular3 += vector2[i];
            denominator1 += vector1[i] * vector1[i];
            denominator2 += vector2[i] * vector2[i];

        }
        double molecular = N*molecular1 - molecular2*molecular3;
        double denominator = Math.sqrt(N*denominator1 - Math.pow(molecular2, 2)) * Math.sqrt(N*denominator2 - Math.pow(molecular3, 2));
        double result = molecular / denominator;

        return result;
    }

}
