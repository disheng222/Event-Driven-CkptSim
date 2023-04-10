import java.util.ArrayList;
import java.util.List;

public class Simulation2 {
	
	public static void main(String[] args)
	{
		double totalLoad = 43200; //7200
		double timeUnit = 2;
		double checkpointCost = 60;
		double recoveryCost = 30;
		double MTBF = 3600;
		int missID = 5;
		
		List<String> resultList = new ArrayList<String>();
		resultList.add("totalLoad = "+totalLoad);
		resultList.add("checkpointCost = "+checkpointCost);
		resultList.add("recoveryCost = "+recoveryCost);
		resultList.add("MTBF = "+MTBF);
		resultList.add("missID = "+missID);
		resultList.add("==============================");
		
		for(int i = 0;i < 1000;i++)
		{
			Job2 job = new Job2(i, missID, totalLoad, timeUnit, checkpointCost, recoveryCost, MTBF, 10*MTBF);
			
			double wTime = job.run();
			String failures = job.printFailureEvents(i, wTime);			
			/*System.out.println("Run "+i+" TotalLoad = "+totalLoad+", optCkptInterval="+job.optCheckpointInterval);
			System.out.println("Run "+i+" wTime = "+wTime+", overhead = "+(wTime-totalLoad)/totalLoad);				
			System.out.println("-----------------------");*/
			resultList.add(failures);
			resultList.add("Run "+i+" TotalLoad = "+totalLoad+", optCkptInterval="+job.optCheckpointInterval);
			resultList.add("Run "+i+" wTime = "+wTime+", overhead = "+(wTime-totalLoad)/totalLoad);
			resultList.add(job.printInfo());
			resultList.add("--------------------------");
		}
		
		PVFile.print2File(resultList, "simulation2.txt");	
		System.out.println("Done.");
	}
	
}
