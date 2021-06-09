import com.github.underscore.lodash.U;
import com.github.underscore.lodash.Xml;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Main implements Cloneable {

    public static void main(String[] args) throws IOException {

    }

    void merge(ArrayList<Integer> a, ArrayList<Integer> b) {
        int n = a.size();
        a.ensureCapacity(2 * n);
        // Если не пуста
        if (n != 0) {
            Queue<Integer> queue = new LinkedList<>();
            int i = 0, j = 0;

            while ((i < n) && (j < n)) {
                if (queue.isEmpty()) {
                    if (a.get(i) <= b.get(j)) {
                        i++;
                    } else {
                        queue.add(a.get(i));
                        a.set(i, b.get(j));
                        i++;
                        j++;
                    }
                } else {

                }
            }
        }
    }
}
