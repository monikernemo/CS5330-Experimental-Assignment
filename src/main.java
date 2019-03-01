import java.util.Random;

public class main {
    public static void main (String args[]) {
        CuckooHashA<Integer> hash = new CuckooHashA<>(400000, 2, 1);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 200000; i++) {
            int j = random.nextInt();
            if (hash.contains(j)) {
                System.out.println("duplicate is of value: " + j);
            }
            hash.add(j);
        }
        System.out.println(hash.getSize());
    }
}
