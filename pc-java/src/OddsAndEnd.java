
public class OddsAndEnd
{
	public static void main(String[] args)
	{
		for (int i = 0; i < 5; i++)
			for (int k = 0; k < 5; k++)
				if (k == 3)
					break;
				else
					System.out.println(i + "" + k);
	}

}
