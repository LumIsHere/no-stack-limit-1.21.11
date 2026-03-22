package com.no_stack_limit;

import java.util.Locale;

public final class StackCountFormatter {
    private StackCountFormatter() {
    }

    public static boolean shouldCompact(int count) {
        return count >= 1_000;
    }

    public static String formatCompact(int count) {
        if (count >= 1_000_000_000) {
            return (count / 1_000_000_000) + "B";
        }
        if (count >= 1_000_000) {
            return (count / 1_000_000) + "M";
        }
        if (count >= 1_000) {
            return (count / 1_000) + "K";
        }
        return Integer.toString(count);
    }

    public static String formatExact(int count) {
        return String.format(Locale.ROOT, "%,d", count);
    }
}
