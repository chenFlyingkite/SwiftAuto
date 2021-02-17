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
        int tn = from.length(); // = max(from, types)
        for (String s : into) {
            tn = Math.max(tn, s.length());
        }
        final String ts = "%" + tn + "s";

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
