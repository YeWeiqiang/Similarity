package sample;

public class CosineSimilarity {
    /**
     *
     * @param vector1
     * @param vector2
     * @return 余弦相似度
     */
    public static double method(double[] vector1, double[] vector2){
        double molecular = 0;   //分子
        double denominator1 = 0;  //分母1
        double denominator2 = 0;  //分母2

        for(int i = 0; i < vector1.length; i++){
            molecular += vector1[i] * vector2[i];
            denominator1 += vector1[i] * vector1[i];
            denominator2 += vector2[i] * vector2[i];
        }
        double result = molecular / Math.sqrt(denominator1 * denominator2);

        return result;

    }

}
