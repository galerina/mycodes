interface DictionaryIterator {
    boolean advance(char c);
    boolean isWord();
    boolean isPrefix();
    String getString();
}
