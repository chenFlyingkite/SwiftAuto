package flyingkite;

import java.util.HashSet;
import java.util.Set;

public class SwiftAuto {
    // https://www.sublimetext.com/docs/1/commands
    // https://www.sublimetext.com/docs/3/key_bindings.html
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
        intExt();
        toDoubleOfCGFloat();
        toRoundedInt();
        toDoubles();
        toStrings();
    }
    // (|U)Int(|8|16|32|64) (++|--)

    private static void intExt() {
        String ind = indent(1);

        ln("extension Int {");
        ln(ind + "public func hex() -> String { return String.init(format:\"%%x\", self); }");
        ln(ind + "public static postfix func ++ (x: inout Int) -> Int { x = x + 1; return x; }");
        ln(ind + "public static postfix func -- (x: inout Int) -> Int { x = x - 1; return x; }");
        ln("}");
        ln();
    }


    private static void toDoubleOfCGFloat() {
        String from = "CGFloat";
        String to = "Double";
        String sign = "lf()";

        String ind = indent(1);
        String comm = ind + "/// Return %s(self)";
        String line = ind + "public func %s -> %s { return %s(self); }";

        ln("extension %s {", from);
        ln(comm, to);
        ln(line, sign, to, to);
        ln("}");
        ln();
    }


    private static void toRoundedInt() {
        // 'Float16' is only available in iOS 14.0 or later
        String[] toRnd = {"Float", "Double", "CGFloat"//, "Float16"
        };
        String to = "Int";

        String ind = indent(1);
        String line = ind + "public func %s -> %s { return %s(self.rounded()); }";
        String signa = "roundedInt()";

        //---- code start
        ln("// Rounding");
        for (String s : toRnd) {
            ln("extension %s {", s);
            ln(line, signa, to, to);
            ln("}");
        }
        ln();
        //---- code end
    }

    private static void toDoubles() {
        final String from = "Double";
        String[] into = {"Int", "Int64", "Float"};
        Set<String> commt = new HashSet<>();
        //String[] into = {"Int"};
        final String[] ops = {"+", "-", "*", "/"};
        final String returns = from;
        final String cons = from; // constructor
        // take the formatter of type as ts
        String ts = ksOf(from, into);

        //---- code start
        ln("// + - * / to Double ");
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
        //---- code end
    }

    public static void toStrings() {
        final String from = "String";
        String[] into = {"Int", "Int64", "Float", "Double"};
        String ts = ksOf(from, into);
        ln("extension %s {", from);
        for (int i = 0; i < into.length; i++) {
            String to = into[i];
            String d1 = indent(1);
            String fmt1 = d1 + "public static func + (lhs: " + ts + ", rhs: " + ts + ") -> %s { return lhs + %s(rhs); }";
            String fmt2 = d1 + "public static func + (lhs: " + ts + ", rhs: " + ts + ") -> %s { return %s(lhs) + rhs; }";
            ln(fmt1, from, to, from, from);
            ln(fmt2, to, from, from, from);
        }
        ln("}");
        ln();
    }

    private static String indent(int n) {
        final String indent = "    ";
        // start working
        final StringBuilder sb = new StringBuilder();
        final StringBuilder s = new StringBuilder(indent);
        int x = n;
        while (x > 0) {
            if ((x & 1) == 1) {
                sb.append(s);
            }
            s.append(s);
            x >>= 1;
        }
        return sb.toString();
    }

    // to be "%ks", with k = max(a_i.length & src.length)
    private static String ksOf(String src, String[] a) {
        // take the formatter of type as ts
        int k = src.length();
        for (String s : a) {
            k = Math.max(k, s.length());
        }
        return "%" + (k > 0 ? k : "") + "s";
    }

    private static void ln() {
        ln("");
    }
    private static void ln(String f, Object... p) {
        System.out.printf((f) + "%n", p);
    }
}
