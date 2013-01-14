package de.eonas.website.rss;

public class Functions {

    private Functions() {
        // Hide constructor.
    }

    public static String stripHtml(String text) {
        return text.replaceAll("\\<.*?\\>", "");
    }

}
