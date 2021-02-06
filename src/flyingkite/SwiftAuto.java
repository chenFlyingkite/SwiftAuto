package flyingkite;

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
        final String from = "Double";
        String[] into = {"Int"};//, "Float", "CGFloat"};
        String[] ops = {"+", "-", "*", "/"};
        final String to = from;
        ln("extension %s {", from);
        for (String op : ops) {
            int d = 1;
            // public static func + (lhs: Double, rhs: Int) -> Double {
            //     return lhs + Double(rhs);
            // }
            for (String ti : into) {
                // small type +-*/ double
                ln("%spublic static func %s (lhs: %s, rhs: %s) -> %s {", indent(d), op, from, ti, to);
                ln("%sreturn lhs %s %s(rhs);", indent(d+1), op, from);
                ln("%s}", indent(d));
            }

            // public static func + (lhs: Int, rhs: Double) -> Double {
            //     return Double(lhs) + rhs;
            // }
            for (String ti : into) {
                // small type +-*/ double
                ln("%spublic static func %s (lhs: %s, rhs: %s) -> %s {", indent(d), op, ti, from, to);
                ln("%sreturn %s(lhs) %s rhs;", indent(d+1), from, op);
                ln("%s}", indent(d));
            }
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
