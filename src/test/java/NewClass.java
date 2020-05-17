
import java.util.LinkedList;
import org.openjfx.collectiveeditor.diff.diff_match_patch;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Diff;
import org.openjfx.collectiveeditor.diff.diff_match_patch.Operation;
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
        testDiffs();
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
            } 
            //At this point, there's either deletion at the start or matching
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
                        } while(len > 0);
                        if (len == 0) j++;
                        else p2.text = p2.text.substring(-len);
                    } else if (len < 0) {
                        //the inverse of the above
                        res.add(p2);
                        j++;
                        do {
                            p1 = d1.get(++i);
                            len += p1.text.length();
                        } while (len < 0);
                        if (len == 0) i++;
                        else p1.text = p1.text.substring(len);
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
                
            }
        }
        while (i < d1.size()) {
            
        }
        while (j < d2.size()) {
            
        }
    }
    
    private static void assertEquals(String error_msg, Object a, Object b) {
        if (!a.toString().equals(b.toString())) {
            throw new Error("assertEquals fail:\n Expected: " + a + "\n Actual: " + b
                      + "\n" + error_msg);
        }
    }

}
