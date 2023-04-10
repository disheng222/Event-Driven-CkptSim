/**
 * This class is to simulate the job run with regular fixed-length checkpointing (optimized by Young's formula)
 * @author sdi
 *
 */
public class Job {

	public static int RUNNING = 0;
	public static int CHECKPOINTING = 1;
	public static int RECOVERING = 2;
	public static int EXIT = 3;
	
	private int status = 0; //0: running, 1: checkpointing, 2: recovering, 3: exit(done)
	
	private double totalLoad = 0;
	private double finishedLoad = 0;
	private double wallClockTime = 0;
	private double checkpointCost = 0;
	private double recoveryCost = 0;
	private double latestCheckpoint = 0;
	private double checkpointProgress = 0;
	private double recoveryProgress = 0;
	private double timeUnit = 0;
	
	double failureEvents[] = null;
	public static int nextFailureID = 0;
	double optCheckpointInterval = 0;
	double nextCheckpointTime = 0;
	
	ExponentialRandomNumberGenerator randomGenerator;
	
	double totalCheckpointTime = 0;
	double totalRecoveryTime = 0;
	double totalRollbackLoss = 0;
	
	public Job(int seed, double totalLoad, double timeUnit, double checkpointCost, double recoveryCost, double MTBF) {
		this.totalLoad = totalLoad;
		this.timeUnit = timeUnit;
		this.checkpointCost = checkpointCost;
		this.recoveryCost = recoveryCost;
		
		nextCheckpointTime = optCheckpointInterval = YoungFormula(checkpointCost, MTBF);
		randomGenerator = new ExponentialRandomNumberGenerator(seed);
		genRandomFailures(200, MTBF);
	}
	
	public double YoungFormula(double C, double Tf)
	{
		return Math.sqrt(2*C*Tf);
	}	
	
	public double[] genRandomFailures(int nbFailures, double Tf)
	{
		double sum = 0;
		double[] result = new double[nbFailures];
		for(int i = 0;i<nbFailures;i++)
		{
			double failureTime = randomGenerator.generateRandomNumber(1/Tf); //Poison process <==> exponential interval
			sum += failureTime;
			result[i] = sum;
		}
		failureEvents = result;
		return result;
	}
	
	public void setNextFailureEvent()
	{
		nextFailureID++;
	}
	
	public double getNextFailureEvent()
	{
		return failureEvents[nextFailureID];
	}
	
	public double getFinishedLoad() {
		return finishedLoad;
	}
	public void setFinishedLoad(double finishedLoad) {
		this.finishedLoad = finishedLoad;
	}
	public double getTimeUnit() {
		return timeUnit;
	}
	public double getTotalLoad() {
		return totalLoad;
	}
	public void setTotalLoad(double totalWorkload) {
		this.totalLoad = totalWorkload;
	};
	public double getRemainingLoad()
	{
		return totalLoad - finishedLoad;
	}
	public void stepForward_Workload()
	{
		finishedLoad += timeUnit;
		wallClockTime += timeUnit;
	}
	
	public boolean timeToFailure()
	{
		if(wallClockTime >= failureEvents[nextFailureID])	
			return true;
		else
			return false;
	}
	
	public boolean timeToCheckpoint()
	{
		if(finishedLoad >= nextCheckpointTime)
			return true;
		else
			return false;
	}
	
	public boolean timeToExit()
	{
		if(finishedLoad >= totalLoad)
			return true;
		else
			return false;
	}
	
	//this run is to simulate the execution with regular periodic checkpointing mechanism (optimal ckpt interval)
	public double run()
	{
		int i = 0;
		nextFailureID = 0;
		while(finishedLoad < totalLoad)
		{
			if(timeToExit())
			{
				status = EXIT;
				break;
			}
			else if(status != RECOVERING && timeToFailure()) //when a failure occurs, stutus --> recovery
			{
				//System.out.println("[LOG] "+wallClockTime+", EVENT: Failure, finishedLoad = "+finishedLoad);
				totalRollbackLoss += (finishedLoad - latestCheckpoint);
				finishedLoad = latestCheckpoint;				
				recoveryProgress = 0;	
				nextFailureID++;
				if(latestCheckpoint!=0)
					status = RECOVERING;
			}
			else if(status != CHECKPOINTING && timeToCheckpoint()) //when it's time to checkpoint, status --> checkpointing
			{
				latestCheckpoint = finishedLoad;
				checkpointProgress = 0;
				status = CHECKPOINTING;
				//System.out.println("[LOG] "+wallClockTime+", EVENT: start checkpointing, finishedLoad = "+finishedLoad);
			}
			
			if(status == RUNNING) //perform running
			{
				finishedLoad += timeUnit;
			}
			else if(status == CHECKPOINTING) //perform checkpointing
			{
				checkpointProgress += timeUnit;
				totalCheckpointTime += timeUnit;
				if(checkpointProgress >= checkpointCost)
				{
					nextCheckpointTime += optCheckpointInterval; //complete checkpointing
					status = RUNNING;
					//System.out.println("[LOG] "+wallClockTime+", EVENT: finish checkpointing ("+checkpointCost+"), back to running status, finishedLoad = "+finishedLoad);					
				}
			}
			else if(status == RECOVERING) //perform recovering
			{
				recoveryProgress += timeUnit;
				totalRecoveryTime += timeUnit;
				if(recoveryProgress >= recoveryCost)
				{
					status = RUNNING;
					//System.out.println("[LOG] "+wallClockTime+", EVENT: finish recovering ("+recoveryCost+"), back to running status, finishedLoad = "+finishedLoad);					
				}
			}
			
			wallClockTime += timeUnit;
		}
		return wallClockTime;
	}
	
	public String printInfo()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("wallClockTime = "+wallClockTime+"\n");
		sb.append("totalCheckpointTime = "+totalCheckpointTime+"\n");
		sb.append("totalRecoveryTime = "+totalRecoveryTime+"\n");	
		sb.append("totalRollbackLoss = "+totalRollbackLoss+"\n");
		sb.append("Breakdown "+totalLoad+" "+totalCheckpointTime+" "+totalRecoveryTime+" "+totalRollbackLoss+" "+wallClockTime+" "+(wallClockTime-totalLoad)/totalLoad+"\n");
		sb.append("breakdown sum_up = "+(totalLoad+totalCheckpointTime+totalRecoveryTime+totalRollbackLoss));
		return sb.toString();
	}
	
	public String printFailureEvents(int j, double endTime)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Run ");
		sb.append(j);
		sb.append(" Failures: ");
		
		for(int i = 0;i<failureEvents.length && failureEvents[i] < endTime;i++)
		{
			sb.append(i);
			sb.append(":");
			sb.append(failureEvents[i]);
			sb.append(" ");
		}
		sb.append("\n");	
		
		/*
		 * System.out.print("Run "+j+" Failures: "); for(int i =
		 * 0;i<failureEvents.length;i++) System.out.print(i+":"+failureEvents[i]+" ");
		 * System.out.println();
		 */
		return sb.toString();
		
	}
	
}
