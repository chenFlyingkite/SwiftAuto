package flyingkite;

import java.util.HashSet;
import java.util.Set;

public class SwiftAuto {
    public static void main(String[] args) {
        imports();
        typeConversion();
    }

    private static void imports() {
        ln();
        ln("import Foundation");
        ln("");
    }

    public static void typeConversion() {
        toDoubles();
    }

    private static void toDoubles() {
        // (|U)Int(|8|16|32|64) (++|--)
        final String from = "Double";
        String[] into = {"Int", "Int64", "Float", "CGFloat"};
        Set<String> commt = new HashSet<>();
        commt.add("CGFloat");
        //String[] into = {"Int"};
        final String[] ops = {"+", "-", "*", "/"};
        final String returns = from;
        final String cons = from; // constructor
        // take the formatter of type as ts
        int tn = from.length(); // = max(from, types)
        for (String s : into) {
            tn = Math.max(tn, s.length());
        }
        final String ts = "%" + tn + "s";

        ln("extension %s {", from);
        for (String small : into) {
            String ind = indent(1);
            String cmm = commt.contains(small) ? "// " : "";
            ln(ind + "// For %s", small);
            for (String op : ops) {
                // small type +-*/ double
                String impl1 = "{ return %s(lhs) %s rhs; }";
                String impl2 = "{ return lhs %s %s(rhs); }";
                // method signature
                String signa = "public static func %s (lhs: " + ts + ", rhs: " + ts + ") -> %s ";
                String line1 = ind + cmm + signa + impl1;
                String line2 = ind + cmm + signa + impl2;
                ln(line1, op, small, from, returns, cons, op);
                ln(line2, op, from, small, returns, op, cons);
            }
            ln();
        }
        ln("}");
    }


    private static String indent(int n) {
        final String indent = "    ";
        // start working
        final StringBuilder sb = new StringBuilder();
        final StringBuilder s = new StringBuilder(indent);
        int x = n;
        while (x > 0) {
            if (x % 2 == 1) {
                sb.append(s);
            }
            s.append(s);
            x >>= 1;
        }
        return sb.toString();
    }

    private static void ln() {
        ln("");
    }
    private static void ln(String f, Object... p) {
        System.out.println(String.format(f, p));
    }
}
