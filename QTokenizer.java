import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QTokenizer {

    private static final Map<Character, Character> SEGMENT_STARTS = Map.of(
            '[', ']',
            '(', ')',
            '{', '}'
    );

    public static void main(String[] args) {

        String nodes = "[FOO][BAR]";

        checkValid(nodes);
    }

    private static List<Token> tokenize(String nodes) {
        char[] chars = nodes.toCharArray();

        int n = chars.length;
        QStack stack = new QStack(n);

        // O(n)
        for (int i = n - 1; i >= 0; --i) { stack.push(chars[i]); }

        StringBuilder tokenBuf = new StringBuilder();

        Character segmentEnd = null;

        List<Token> result = new LinkedList<>();


        // O(n)
        for (int i = 0; i < n; ++i) {

            char ch = stack.pop();

            Character close = SEGMENT_STARTS.get(ch);

            if (close != null) {

                ensureLiteralEnd(tokenBuf, result);

                result.add(new Token(Type.VARIABLE_PREFIX, "'"+ch+"'"));

                segmentEnd = close;
            } else if (Objects.equals(ch, segmentEnd)) { // end

                ensureLiteralEnd(tokenBuf, result);

                result.add(new Token(Type.VARIABLE_SUFFIX, "'"+ch+"'"));

                segmentEnd = null;

            } else {
                tokenBuf.append(ch);
            }
        }
        return result;
    }

    // O(n)
    public static void checkValid(String nodes) {
        State parsingState = State.START;

        for (Token token : tokenize(nodes)) {
            switch (token.type()) {
                case VARIABLE_PREFIX -> {
                    switch (parsingState) {
                        case START, VAR_NAME_END -> parsingState = State.VAR_NAME_START;

                        default -> throw new IllegalStateException("Unexpected prefix delimiter!");
                    }
                }
                case VARIABLE_SUFFIX -> parsingState = State.VAR_NAME_END;
                case LITERAL -> {
                    if (parsingState == State.VAR_NAME_START) {
                        parsingState = State.VAR_NAME_END;
                    } else {
                        throw new IllegalStateException("Unexpected literal!");
                    }
                }
            }
        }

        if (parsingState != State.VAR_NAME_END) {
            throw new IllegalStateException("Unexpected end of string!");
        }
    }

    private static void ensureLiteralEnd(StringBuilder tokenBuf, List<Token> tokens) {
        if (!tokenBuf.isEmpty()) {
            tokens.add(new Token(Type.LITERAL, tokenBuf.toString()));

            tokenBuf.delete(0, tokenBuf.length());
        }
    }

    private record Token(
            Type type,
            String value
    ) { }

    private enum Type {
        VARIABLE_PREFIX,
        VARIABLE_SUFFIX,
        LITERAL
    }

    private enum State {
        START,
        VAR_NAME_START,
        VAR_NAME_END,
    }
}