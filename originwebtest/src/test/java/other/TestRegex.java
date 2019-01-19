package other;

import org.junit.Test;

public class TestRegex {

    @Test
    public void testZ() {
        String card1 = "654234231";
        String regex1 = "\\d{9}";
        System.out.println(card1.matches(regex1));

        String card2 = "654234/23/1";
        String regex2 = "\\d{6}/\\d{2}/\\d";
        System.out.println(card2.matches(regex2));

        String card3 = "654234 23 1";
        String regex3 = "\\d{6} \\d{2} \\d";
        System.out.println(card3.matches(regex3));

        String regex = "\\d{9}|\\d{6}/\\d{2}/\\d|\\d{6} \\d{2} \\d";
        System.out.println(card1.matches(regex));
        System.out.println(card2.matches(regex));
        System.out.println(card3.matches(regex));
    }
}
