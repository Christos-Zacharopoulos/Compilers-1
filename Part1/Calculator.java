import java.io.InputStream;
import java.io.IOException;

class Calculator {

    private int lookaheadToken;

    private InputStream in;

    public Calculator(InputStream in) throws IOException {

        this.in = in;
        lookaheadToken = in.read();
    }

    /// Helper functions here. \\\
    private int evalDigit(int digit){
        return digit - '0';
    }

    private boolean isEOF() {
        return (lookaheadToken == '\n' || lookaheadToken == -1);
    }

    private boolean isDigit(){
        return (lookaheadToken > '0' || lookaheadToken < '9' );
    }

    private boolean isOpenParenthesis(){
        return (lookaheadToken == '(');
    }

    private boolean isCloseParenthesis(){
        return (lookaheadToken == ')');
    }

    private boolean isPlus(){
        return (lookaheadToken == '+');
    }

    private boolean isMinus(){
        return (lookaheadToken == '-');
    }

    private boolean isOpLow(){
        return (isPlus() || isMinus());
    }

    private boolean isMult(){
        return (lookaheadToken == '*');
    }

    private boolean isDiv(){
        return (lookaheadToken == '/');
    }

    private boolean isOpHigh(){
        return (isMult() || isDiv());
    }


    private void throwError() throws IOException, ParseError {
        throw new ParseError();
    }

    private void consume(int symbol) throws IOException, ParseError {

        if (lookaheadToken != symbol) {
            throwError();
        }
        lookaheadToken = in.read();
    }

    /// Calculator fun all the way from here. \\\
    private float Num() throws IOException, ParseError {

        float result = 0.0f;

        do {
//            int int_res = evalDigit(lookaheadToken);
            result = (result * 10 )+ (float) evalDigit(lookaheadToken);
            consume(lookaheadToken);

        } while (isDigit());
        return result;
    }

    private float Factor() throws IOException, ParseError {
        if (isOpenParenthesis()) {
            consume(lookaheadToken);
            float exp = Exp();

            if (!isCloseParenthesis())
                throwError();
            else {
                consume(lookaheadToken);
                return exp;
            }
        }

        return Num();
    }

    private float Term1(float param) throws IOException, ParseError {

        if (  isCloseParenthesis() || isEOF() || isOpLow())
            return param;
        else if ( !isOpHigh() )
            throwError();


        float result = 0.0f;
        if (isMult()) {
            consume(lookaheadToken);
            result = param * Factor();
        }
        else if (isDiv()) {
            consume(lookaheadToken);
            result = param / Factor();
        }

        return Term1(result);
    }

    private float Term() throws IOException, ParseError {
        if (!isOpenParenthesis() && !isDigit())
            throwError();

        float factor = Factor();

        return Term1(factor);
    }

    private float Prod(float param) throws IOException, ParseError {

        if ( isCloseParenthesis() || isEOF() )
            return param;
        else if ( !isOpLow() )
            throwError();


        float result = 0.0f;
        if (isPlus()) {
            consume(lookaheadToken);
            result = param + Term();
        }
        else if (isMinus()) {
            consume(lookaheadToken);
            result = param - Term();
        }

        return Prod(result);
    }

    private float Exp() throws IOException, ParseError {

        if (!isOpenParenthesis() && !isDigit())
            throwError();

        float term = Term();

        return Prod(term);
    }

    public float eval() throws IOException, ParseError {
        float result = Exp();

        if (!isEOF())
            throwError();

        return result;
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
