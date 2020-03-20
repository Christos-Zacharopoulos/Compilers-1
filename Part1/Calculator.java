import java.io.InputStream;
import java.io.IOException;

class Calculator {

    private int lookaheadToken;

    private InputStream in;

    public Calculator(InputStream in) throws IOException {

        this.in = in;
        lookaheadToken = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {

        if (lookaheadToken != symbol) {
            throw new ParseError();
        }
        lookaheadToken = in.read();
    }

    private int evalDigit(int digit){
        return digit - '0';
    }

    private boolean isEOF() {
        return (lookaheadToken == '\n' || lookaheadToken == -1);
    }

    private boolean isDigit(){
        return (lookaheadToken > '0' || lookaheadToken < '9' );
    }

    private int Tern() throws IOException, ParseError {
        if(!isDigit()) {
            throw new ParseError();
        }
        int cond = evalDigit(lookaheadToken);
        consume(lookaheadToken);
        return TernTail(cond);
    }

    private int TernTail(int cond) throws IOException, ParseError {

        if(lookaheadToken == ':' || isEOF()) {
            return cond;
        }
        else if(lookaheadToken != '?'){
            throw new ParseError();
        }

        consume('?');
        int thenPart = Tern();
        consume(':');
        int elsePart = Tern();

        return cond != 0 ? thenPart : elsePart;
    }

    public int eval() throws IOException, ParseError {
        int rv = Tern();
        if (!isEOF())
            throw new ParseError();
        return rv;
    }

    public static void main(String[] args) {
        try {
            Calculator evaluate = new Calculator(System.in);
            System.out.println(evaluate.eval());
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch(ParseError err){
            System.err.println(err.getMessage());
        }
    }
}
