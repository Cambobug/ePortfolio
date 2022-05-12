package ePortfolio;

import java.util.ArrayList;

public class InputChecking {
    
    public InputChecking()
    {

    }

    /**
     * inputParser is meant to be a dynamic input error checker that the programmer can change depending on what kind of input they intend to error check (assumes that the when given an array of strings the arguments are in order string,int,double)
     * @param input an array of strings as input
     * @param numStrings however many strings are supposed to be in the array
     * @param numInts however many integers are supposed to be in the array
     * @param numDoubs however many doubles are supposed to be in the array
     * @return false if any of the elements in the string array are incorrect inputs, true if none of the inputs are incorrect
     */
    protected boolean inputParser(ArrayList<String> input, int numStrings, int numInts, int numDoubs)
    {
        int index = 0;

        if(input.size() != (numStrings + numInts + numDoubs))
        {
            return false;
        }

        while(index < numStrings)
        {
            if (input.get(index).isEmpty())
            {
                return false;
            }
            index++;
        }

        while(index < (numStrings + numInts))
        {
            if(!input.get(index).isEmpty())
            {
                try
                {
                    int quan = Integer.parseInt(input.get(index));
                    if(quan < 1)
                    {
                        throw new NullPointerException();
                    }
                }
                catch(Exception e)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            index++;
        }

        while(index < (numStrings + numInts + numDoubs))
        {
            if(!input.get(index).isEmpty())
            {
                try
                {
                    double price = Double.parseDouble(input.get(index));
                    if(price <= 0)
                    {
                        throw new NullPointerException();
                    }
                }
                catch(Exception e)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
            index++;
        }

        return true;
    }
}
