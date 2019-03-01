import java.util.Random;
import java.math.BigInteger;

/**
 * Constructs modulo hash functions in the form of
 * (ax  + b mod p) mod m
 */
public class HashFunction {

        private long a;
        private long b;
        private long p;
        private int m;
        private Random random;


        public HashFunction (int m) {
            this.random = new Random(System.currentTimeMillis());
            this.m = m;
            this.a = Math.abs(random.nextInt());
            //System.out.println(a);
            this.b = Math.abs(random.nextInt());
            //System.out.println(b);
            this.p = new BigInteger(String.format("%d", Math.round(m * (1 + random.nextDouble()))))
                    .nextProbablePrime().longValue();
            //System.out.println(p);

        }

        public long getP() {
            return p;
        }

        public int evaluate(long n) {
            return ( (Math.toIntExact((a*n + b) % p) % m) + m) % m;
        }
}
