
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import org.openjfx.collectiveeditor.diff.diff_match_patch;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Diff;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Operation;
import org.openjfx.collectiveeditor.logic.Change;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author max
 */
public class NewClass {

    public static void main(String[] args) {
        Change c = new Change();
        c.addDeletion(5);
        c.addInsertion(3, "Hello");
        long t0 = System.currentTimeMillis();
        byte[] cs = c.toBytes();
        long t1 = System.currentTimeMillis();
        System.out.println(cs.length + ", time serializing: " + (t1 - t0));
        t0 = System.currentTimeMillis();
        c = Change.fromBytes(cs);
        t1 = System.currentTimeMillis();
        if (c == null) {
            System.out.println("Bad deserialize");
            System.exit(-1);
        }
        cs = c.toBytes();
        System.out.println(cs.length + ", time deserializing: " + (t1 - t0));
    }

    static void testDiffs() {
        //It hurts to see a class without a capital letter first but hey, 
        //do it for compatibility
        diff_match_patch dmp = new diff_match_patch();

    }

    /* p1 antes que p2 */
    static void mergeDiffs(LinkedList<Diff> d1, LinkedList<Diff> d2) {
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<Diff> res = new LinkedList<>();
        int i = 0, j = 0;
        Diff p1, p2;
        while (i < d1.size() && j < d2.size()) {
            p1 = d1.get(i);
            p2 = d2.get(j);
            if (p1.operation == Operation.INSERT) {
                res.add(p1);
                i++;
            } else if (p2.operation == Operation.INSERT) {
                res.add(p2);
                j++;
            } //At this point, there's either deletion at the start or matching
            else if (p1.operation == Operation.DELETE) {
                if (p2.operation == Operation.DELETE) {
                    //Both remove at the start
                    int len = p1.text.length() - p2.text.length();
                    if (len > 0) {
                        //len is the number of chars that have to be removed from
                        //p2's next token after its delete (cause p1 is bigger)
                        res.add(p1);
                        i++;
                        do {
                            p2 = d2.get(++j);
                            len -= p2.text.length();
                        } while (len > 0);
                        if (len == 0) {
                            j++;
                        } else {
                            p2.text = p2.text.substring(-len);
                        }
                    } else if (len < 0) {
                        //the inverse of the above
                        res.add(p2);
                        j++;
                        do {
                            p1 = d1.get(++i);
                            len += p1.text.length();
                        } while (len < 0);
                        if (len == 0) {
                            i++;
                        } else {
                            p1.text = p1.text.substring(len);
                        }
                    } else {
                        //Both remove the same piece.
                        res.add(p1);
                        i++;
                        j++;
                    }
                } else {
                    //p2 doesn't remove at the start
                    int len = p1.text.length() - p2.text.length();
                    if (len > 1) {
                        //p1 removes at the start and p2 removes at the end (merge)
                        res.add(p1);
                        i++;
                        j++; //p2's post is not relevant bc it's removed entirely
                        p2 = d2.get(j);
                        p2.text = p2.text.substring(len);
                    }
                }
            } else if (p2.operation == Operation.DELETE) {

            } else { //0

            }
        }
        while (i < d1.size()) {

        }
        while (j < d2.size()) {

        }
    }
    //NOT COMPLETED
    static void removeDiff(LinkedList<Diff> d1, LinkedList<Diff> d2, int i, int j) {
        int len = 0;
        Diff p2;
        i++;
        do {
            p2 = d2.get(++j);
            len -= p2.text.length();
        } while (len > 0);
        if (len == 0) {
            j++;
        } else {
            p2.text = p2.text.substring(-len);
        }
    }

    private static void assertEquals(String error_msg, Object a, Object b) {
        if (!a.toString().equals(b.toString())) {
            throw new Error("assertEquals fail:\n Expected: " + a + "\n Actual: " + b
                    + "\n" + error_msg);
        }
    }

}
