package io.github.opencubicchunks.cubicchunks.cubicgen.falling;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.Char2LongMap;
import it.unimi.dsi.fastutil.chars.Char2LongOpenHashMap;

/**
 * @author DaPorkchop_
 */
public class Digits {
    public static final Char2LongMap ALL_DIGITS = new Char2LongOpenHashMap();

    public static final int DIGIT_W = 6;
    public static final int DIGIT_H = 10;

    private static void registerDigit(char digit, String[] contents) {
        long l = 0L;
        Preconditions.checkArgument(contents.length == DIGIT_H);
        for (int z = 0; z < DIGIT_H; z++) {
            Preconditions.checkArgument(contents[z].length() == DIGIT_W);
            for (int x = 0; x < DIGIT_W; x++) {
                if (contents[z].charAt(x) != ' ') {
                    l |= 1L << (z * DIGIT_W + x);
                }
            }
        }
        ALL_DIGITS.put(digit, l);
    }

    public static void register() {
        ALL_DIGITS.clear();
        registerDigit('0', new String[]{
                " #### ",
                "######",
                "##  ##",
                "##  ##",
                "##  ##",
                "##  ##",
                "##  ##",
                "##  ##",
                "######",
                " #### ",
        });
        registerDigit('1', new String[]{
                "  ##  ",
                " ###  ",
                "####  ",
                "  ##  ",
                "  ##  ",
                "  ##  ",
                "  ##  ",
                "  ##  ",
                "######",
                "######",
        });
        registerDigit('2', new String[]{
                " #### ",
                "######",
                "##  ##",
                "    ##",
                "   ## ",
                "  ##  ",
                " ##   ",
                "##    ",
                "######",
                "######",
        });
        registerDigit('3', new String[]{
                " #### ",
                "######",
                "##  ##",
                "    ##",
                "  ### ",
                "  ### ",
                "    ##",
                "##  ##",
                "######",
                " #### ",
        });
        registerDigit('4', new String[]{
                "##  ##",
                "##  ##",
                "##  ##",
                "##  ##",
                "######",
                "######",
                "    ##",
                "    ##",
                "    ##",
                "    ##",
        });
        registerDigit('5', new String[]{
                "######",
                "######",
                "##    ",
                "##### ",
                " #####",
                "    ##",
                "##  ##",
                "##  ##",
                "######",
                " #### ",
        });
        registerDigit('6', new String[]{
                " #### ",
                "######",
                "##  ##",
                "##    ",
                "##### ",
                "######",
                "##  ##",
                "##  ##",
                "######",
                " #### ",
        });
        registerDigit('7', new String[]{
                "######",
                "######",
                "    ##",
                "    ##",
                "   ## ",
                "   ## ",
                "  ##  ",
                "  ##  ",
                " ##   ",
                " ##   ",
        });
        registerDigit('8', new String[]{
                " #### ",
                "######",
                "##  ##",
                "##  ##",
                "######",
                "######",
                "##  ##",
                "##  ##",
                "######",
                " #### ",
        });
        registerDigit('9', new String[]{
                " #### ",
                "######",
                "##  ##",
                "##  ##",
                "######",
                " #####",
                "    ##",
                "##  ##",
                "######",
                " #### ",
        });
        registerDigit('-', new String[]{
                "      ",
                "      ",
                "      ",
                "      ",
                "######",
                "######",
                "      ",
                "      ",
                "      ",
                "      ",
        });
    }

    static {
        register();
    }
}
