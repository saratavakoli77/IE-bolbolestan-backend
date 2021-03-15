package bolbolestan.middlewares;

public class SearchHistory {
    private static String lastSearch = "";

    public static void setLastSearch(String newSearch) {
        lastSearch = newSearch;
    }

    public static String getLastSearch() {
        return lastSearch;
    }
}
