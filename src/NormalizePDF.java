import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NormalizePDF {

	
	public static void main(String[] args)
	{
		if(args.length<1)
		{
			System.out.println("Usage: java NormalizePDF [pdf file]");
			System.exit(0);
		}
		
		String file = "/home/sdi/Development/eclipse-workspace/TimeMachineSimJob_DSN/1000-tests/totalLoad21600/pdf/simulation2_4000_result.csv_6.dis";
		
		file = args[0];
		System.out.println("pdf file: "+file);
		
		List<String> list = PVFile.readFile(file);
		
		float max = 0;
		Iterator<String> iter = list.iterator();
		while(iter.hasNext())
		{
			String s = iter.next();
			float value = Float.valueOf(s.split("\\s")[1]);
			if(value>max)
				max = value;
		}
		
		System.out.println("max="+max);

		List<String> resultList = new ArrayList<String>();
		iter = list.iterator();
		while(iter.hasNext())
		{
			String s = iter.next();
			float value = Float.valueOf(s.split("\\s")[1]);
			float nValue = value/max;
			resultList.add(s+" "+nValue);
		}		
		
		PVFile.print2File(resultList, file+".ndis");
	}
}
