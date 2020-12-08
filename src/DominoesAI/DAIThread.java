package DominoesAI;

import DominoesClient.DCInterface;
import DominoesClient.DCThread;

public class DAIThread extends DCThread
{
    public DAIThread(DCInterface dcInterface)
    {
        super(generatePseudonym(), dcInterface);
    }

    @Override
    public void run()
    {
        // TODO: AGENT LOGIC
    }

    private static String generatePseudonym()
    {
        // TODO: PSEUDONYM GENERATION
        return "";
    }
}
