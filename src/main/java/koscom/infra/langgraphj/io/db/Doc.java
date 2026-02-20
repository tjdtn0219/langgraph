package koscom.infra.langgraphj.io.db;

import java.time.LocalDate;

public final class Doc {
    private final int id;
    private final String title;
    private final String content;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String manager;

    public Doc(int id, String title, String content, LocalDate fromDate, LocalDate toDate, String manager) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.manager = manager;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }
    public String getManager() { return manager; }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", manager='" + manager + '\'' +
                '}';
    }
}
