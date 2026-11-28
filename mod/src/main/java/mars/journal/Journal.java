package mars.journal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The journal's pure logic: which entries a token unlocks, text with its data values filled in, and
 * the pages of the book as plain segments the Minecraft side turns into a written book. Cost:
 * linear in the number of entries (about 8 now); only on an unlock or a book open.
 */
public final class Journal {
  private final List<JournalEntry> entries;

  public Journal(List<JournalEntry> entries) {
    this.entries = List.copyOf(entries);
  }

  public List<JournalEntry> entries() {
    return entries;
  }

  public JournalEntry get(String id) {
    for (JournalEntry e : entries) {
      if (e.id().equals(id)) {
        return e;
      }
    }
    return null;
  }

  /** The entries a trigger token unlocks (a planet-scope entry for any player on the planet). */
  public List<JournalEntry> fired(String token) {
    List<JournalEntry> out = new ArrayList<>();
    for (JournalEntry e : entries) {
      if (e.triggers().contains(token)) {
        out.add(e);
      }
    }
    return out;
  }

  /** Replace {name} placeholders. A placeholder with no value is an error, never left in text. */
  public static String fill(String text, Map<String, String> params) {
    String out = text;
    for (Map.Entry<String, String> p : params.entrySet()) {
      out = out.replace("{" + p.getKey() + "}", p.getValue());
    }
    if (out.contains("{") && out.contains("}")) {
      throw new IllegalStateException("Unfilled placeholder in journal text: " + out);
    }
    return out;
  }

  /** One piece of a page: text, optional bold, optional jump to a page number (1 based). */
  public record Seg(String text, boolean bold, int jumpToPage) {
    public static Seg plain(String t) {
      return new Seg(t, false, 0);
    }

    public static Seg bold(String t) {
      return new Seg(t, true, 0);
    }
  }

  /** The book: the title and the pages, each a list of segments. */
  public record Book(String title, List<List<Seg>> pages) {}

  /** About 14 lines of 19 characters fit a page (landing-spec 5.1); this is a safe budget. */
  public static final int PAGE_CHARS = 200;

  /**
   * Build the book for a player. Page 1 is the contents: unlocked entries by section, the unread
   * ones marked "New", each a jump to its first page. Then each unlocked entry, in unlock order,
   * on as many pages as it needs. {@code hs} adds the high-school "Read more" page.
   */
  public Book build(
      JournalState state, boolean hs, Map<String, String> params, String bookTitle, String newWord) {
    List<JournalEntry> shown = new ArrayList<>();
    for (String id : state.unlocked()) {
      JournalEntry e = get(id);
      if (e != null) {
        shown.add(e);
      }
    }
    List<List<Seg>> body = new ArrayList<>();
    Map<String, Integer> firstPage = new LinkedHashMap<>();
    int bodyStart = 2; // page 1 is the contents (it never needs more than one page for 8 entries)
    for (JournalEntry e : shown) {
      firstPage.put(e.id(), bodyStart + body.size());
      List<String> chunks = new ArrayList<>();
      StringBuilder main = new StringBuilder();
      main.append(fill(e.text(), params));
      if (e.fictionNote() != null) {
        main.append(' ').append(fill(e.fictionNote(), params));
      }
      if (e.earthLine() != null) {
        main.append(' ').append(fill(e.earthLine(), params));
      }
      chunks.addAll(split(main.toString(), PAGE_CHARS));
      if (hs && e.hsMore() != null) {
        chunks.addAll(split(fill(e.hsMore(), params), PAGE_CHARS));
      }
      for (int i = 0; i < chunks.size(); i++) {
        List<Seg> page = new ArrayList<>();
        if (i == 0) {
          page.add(Seg.bold(e.title() + "\n"));
        }
        page.add(Seg.plain(chunks.get(i)));
        body.add(page);
      }
    }
    List<List<Seg>> pages = new ArrayList<>();
    List<Seg> contents = new ArrayList<>();
    contents.add(Seg.bold(bookTitle + "\n"));
    List<String> unread = state.unread();
    for (String section : JournalEntry.SECTIONS) {
      boolean header = false;
      for (JournalEntry e : shown) {
        if (!e.section().equals(section)) {
          continue;
        }
        if (!header) {
          contents.add(Seg.plain("\n" + section + "\n"));
          header = true;
        }
        String mark = unread.contains(e.id()) ? " (" + newWord + ")" : "";
        contents.add(new Seg(e.title() + mark + "\n", false, firstPage.get(e.id())));
      }
    }
    pages.add(contents);
    pages.addAll(body);
    return new Book(bookTitle, List.copyOf(pages));
  }

  /** Split on word boundaries into chunks of at most {@code max} characters. */
  public static List<String> split(String text, int max) {
    List<String> out = new ArrayList<>();
    StringBuilder cur = new StringBuilder();
    for (String word : text.split(" ")) {
      if (cur.length() > 0 && cur.length() + 1 + word.length() > max) {
        out.add(cur.toString());
        cur.setLength(0);
      }
      if (cur.length() > 0) {
        cur.append(' ');
      }
      cur.append(word);
    }
    if (cur.length() > 0) {
      out.add(cur.toString());
    }
    return out;
  }
}
